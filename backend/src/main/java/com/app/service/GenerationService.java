package com.app.service;

import com.app.dto.JobSubmissionResponse;
import com.app.dto.SpecRequest;
import com.app.dto.StatusResponse;
import com.app.model.GeneratedOutput;
import com.app.model.GenerationJob;
import com.app.model.JobStatus;
import com.app.repo.GeneratedOutputRepository;
import com.app.repo.GenerationJobRepository;
import com.app.service.aifactory.AICustomException;
import com.app.service.aifactory.AIService;
import com.app.service.aifactory.AIServiceFactory;
import com.app.mapper.SpecRequestMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class GenerationService {

    public static final String JOB_NOT_FOUND_FOR_PROCESSING = "GenerationJob with ID {} not found for async processing.";
    public static final String AI_SERVICE_ERROR = "AI Service Error: ";
    public static final String RESPONSE_PARSING_ERROR = "Response Parsing Error: ";
    public static final String UNEXPECTED_ERROR = "Unexpected Error: ";
    public static final String JOB_NOT_FOUND_STATUS = "Job not found";

    private final GenerationJobRepository generationJobRepository;
    private final GeneratedOutputRepository generatedOutputRepository;
    private final AIServiceFactory aiServiceFactory;
    private final ObjectMapper objectMapper;
    private final SpecRequestMapper specRequestMapper;

    private GenerationService self;

    @Autowired
    public void setSelf(@Lazy GenerationService self) {
        this.self = self;
    }

    public GenerationService(GenerationJobRepository generationJobRepository,
                             GeneratedOutputRepository generatedOutputRepository,
                             AIServiceFactory aiServiceFactory,
                             ObjectMapper objectMapper,
                             SpecRequestMapper specRequestMapper
    ) {
        this.generationJobRepository = generationJobRepository;
        this.generatedOutputRepository = generatedOutputRepository;
        this.aiServiceFactory = aiServiceFactory;
        this.objectMapper = objectMapper;
        this.specRequestMapper = specRequestMapper;
    }

    @Transactional
    public JobSubmissionResponse startGenerationJob(SpecRequest specRequestDto) {
        GenerationJob job = new GenerationJob();
        job.setSpecRequest(specRequestMapper.toEntity(specRequestDto));
        job.setStatus(JobStatus.PENDING);
        job = generationJobRepository.save(job);

        self.processGeneration(job.getId());

        return new JobSubmissionResponse(job.getId());
    }

    @Async
    @Transactional
    public void processGeneration(UUID jobId) {
        Optional<GenerationJob> jobOptional = generationJobRepository.findById(jobId);
        if (jobOptional.isEmpty()) {
            log.error(JOB_NOT_FOUND_FOR_PROCESSING, jobId);
            return;
        }

        GenerationJob job = jobOptional.get();
        job.setStatus(JobStatus.RUNNING);
        generationJobRepository.save(job);
        log.info("GenerationJob {} status set to RUNNING.", jobId);

        try {
            AIService aiService = aiServiceFactory.getAIService(job.getSpecRequest().isRealModel());
            String generatedJson = aiService.generate(specRequestMapper.toDto(job.getSpecRequest()));

            GeneratedOutput generatedOutput = objectMapper.readValue(generatedJson, GeneratedOutput.class);
            generatedOutput.setGenerationJob(job);
            generatedOutputRepository.save(generatedOutput);

            job.setStatus(JobStatus.COMPLETE);
            generationJobRepository.save(job);
            log.info("GenerationJob {} completed successfully.", jobId);

        } catch (AICustomException e) {
            log.error("AI Service failed for job {}: {}", jobId, e.getMessage());
            job.setStatus(JobStatus.FAILED);
            job.setErrorMessage(AI_SERVICE_ERROR + e.getMessage());
            generationJobRepository.save(job);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse AI response for job {}: {}", jobId, e.getMessage());
            job.setStatus(JobStatus.FAILED);
            job.setErrorMessage(RESPONSE_PARSING_ERROR + e.getMessage());
            generationJobRepository.save(job);
        } catch (Exception e) {
            log.error("An unexpected error occurred during generation for job {}: {}", jobId, e.getMessage(), e);
            job.setStatus(JobStatus.FAILED);
            job.setErrorMessage(UNEXPECTED_ERROR + e.getMessage());
            generationJobRepository.save(job);
        }
    }

    @Transactional(readOnly = true)
    public StatusResponse getJobStatus(UUID jobId) {
        return generationJobRepository.findById(jobId)
                .map(job -> new StatusResponse(job.getStatus().name(), job.getErrorMessage()))
                .orElse(new StatusResponse(JobStatus.FAILED.name(), JOB_NOT_FOUND_STATUS));
    }

    @Transactional(readOnly = true)
    public Optional<GeneratedOutput> getGeneratedOutput(UUID jobId) {
        return generatedOutputRepository.findById(jobId);
    }
}
