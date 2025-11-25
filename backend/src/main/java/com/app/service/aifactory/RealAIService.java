package com.app.service.aifactory;

import com.app.dto.SpecRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
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
                    .block(); // Blocking for simplicity as per current AIService interface
        } catch (WebClientResponseException e) {
            throw new AICustomException("AI Service API returned an error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new AICustomException("Failed to communicate with AI Service API: " + e.getMessage(), e);
        }
    }
}
