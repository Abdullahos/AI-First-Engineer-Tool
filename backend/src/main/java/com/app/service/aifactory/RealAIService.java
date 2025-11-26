package com.app.service.aifactory;

import com.app.dto.SpecRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.util.retry.Retry;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Service
@Slf4j
@Profile("!test")
public class RealAIService implements AIService {

    private final WebClient webClient;
    private final String aiApiUrl;

    public RealAIService(WebClient.Builder webClientBuilder, @Value("${ai.api.url}") String aiApiUrl) {
        this.webClient = webClientBuilder.build();
        this.aiApiUrl = aiApiUrl;
    }

    @Override
    public String generate(SpecRequest specRequest) throws AICustomException {
        try {
            return webClient.post()
                    .uri(aiApiUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(specRequest)
                    .retrieve()
                    .bodyToMono(String.class)
                    .retryWhen(Retry.backoff(10, Duration.ofSeconds(5))
                            .maxBackoff(Duration.ofSeconds(60))
                            .filter(this::isRetryableException)
                            .doBeforeRetry(retrySignal -> log.debug("Retrying AI call (attempt {}/{}) due to: {}",
                                    retrySignal.totalRetries() + 1, 10, retrySignal.failure().getMessage())))
                    .block();
        } catch (WebClientResponseException e) {
            log.error("AI Service API returned an error after all retries: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new AICustomException("AI Service API returned an error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            log.error("Failed to communicate with AI Service API after all retries: {}", e.getMessage(), e);
            throw new AICustomException("Failed to communicate with AI Service API: " + e.getMessage(), e);
        }
    }

    private boolean isRetryableException(Throwable throwable) {
        if (throwable instanceof WebClientResponseException) {
            HttpStatusCode statusCode = ((WebClientResponseException) throwable).getStatusCode();
            return statusCode == HttpStatus.TOO_MANY_REQUESTS || // 429
                   statusCode == HttpStatus.INTERNAL_SERVER_ERROR || // 500
                   statusCode == HttpStatus.SERVICE_UNAVAILABLE; // 503
        }

        return throwable instanceof TimeoutException ||
               throwable instanceof IOException;
    }
}
