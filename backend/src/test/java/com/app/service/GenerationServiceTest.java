package com.app.service;

import com.app.dto.JobSubmissionResponse;
import com.app.dto.SpecRequest;
import com.app.dto.StatusResponse;
import com.app.mapper.SpecRequestMapper;
import com.app.model.GeneratedOutput;
import com.app.model.GenerationJob;
import com.app.model.JobStatus;
import com.app.repo.GeneratedOutputRepository;
import com.app.repo.GenerationJobRepository;
import com.app.service.aifactory.AICustomException;
import com.app.service.aifactory.AIService;
import com.app.service.aifactory.AIServiceFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.app.service.GenerationService.*;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration," +
                "org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration," +
                "org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration"
})
class GenerationServiceTest {

    @Autowired
    private GenerationService generationService;

    @MockBean
    private GenerationJobRepository generationJobRepository;

    @MockBean
    private GeneratedOutputRepository generatedOutputRepository;

    @MockBean
    private AIServiceFactory aiServiceFactory;

    @MockBean
    private ObjectMapper objectMapper;

    @MockBean
    private SpecRequestMapper specRequestMapper;

    @MockBean
    private AIService aiService;

    @Autowired
    private ApplicationContext applicationContext;
    private GenerationService proxiedGenerationService;

    @BeforeEach
    void setUp() {
        reset(generationJobRepository, generatedOutputRepository, aiServiceFactory, objectMapper, specRequestMapper, aiService);
        proxiedGenerationService = applicationContext.getBean(GenerationService.class);

        when(aiServiceFactory.getAIService(anyBoolean())).thenReturn(aiService);

        ObjectReader mockObjectReader = mock(ObjectReader.class);
        ObjectWriter mockObjectWriter = mock(ObjectWriter.class);

        when(objectMapper.reader()).thenReturn(mockObjectReader);
        when(objectMapper.writer()).thenReturn(mockObjectWriter);

        when(mockObjectReader.forType(any(Class.class))).thenReturn(mockObjectReader);
        when(mockObjectWriter.forType(any(Class.class))).thenReturn(mockObjectWriter);


        when(specRequestMapper.toEntity(any(SpecRequest.class))).thenAnswer(invocation -> {
            com.app.model.SpecRequest newEntity = new com.app.model.SpecRequest();
            newEntity.setSystemDescription("Test System");
            newEntity.setFunctionalRequirements(Arrays.asList("FR1", "FR2"));
            newEntity.setNonFunctionalRequirements(Arrays.asList("NFR1"));
            newEntity.setDataModel("Test Data Model");
            newEntity.setApiDesign("Test API Design");
            newEntity.setAcceptanceCriteria(Arrays.asList("AC1"));
            newEntity.setRealModel(false);
            return newEntity;
        });

        when(specRequestMapper.toDto(any(com.app.model.SpecRequest.class))).thenReturn(createValidSpecRequestDto());
    }

    private SpecRequest createValidSpecRequestDto() {
        SpecRequest specRequest = new SpecRequest();
        specRequest.setSystemDescription("Test System");
        specRequest.setFunctionalRequirements(Arrays.asList("FR1", "FR2"));
        specRequest.setNonFunctionalRequirements(Arrays.asList("NFR1"));
        specRequest.setDataModel("Test Data Model");
        specRequest.setApiDesign("Test API Design");
        specRequest.setAcceptanceCriteria(Arrays.asList("AC1"));
        specRequest.setRealModel(false);
        return specRequest;
    }

    private com.app.model.SpecRequest createValidSpecRequestEntity() {
        return specRequestMapper.toEntity(createValidSpecRequestDto());
    }

    private GenerationJob createGenerationJob(UUID id, JobStatus status, com.app.model.SpecRequest specRequest) {
        GenerationJob job = new GenerationJob();
        job.setId(id);
        job.setStatus(status);
        job.setSpecRequest(specRequest);
        return job;
    }

    private GeneratedOutput createGeneratedOutput(UUID id, GenerationJob job) {
        GeneratedOutput output = new GeneratedOutput();
        output.setGenerationJob(job);
        output.setBackendCode("backend code");
        output.setFrontendCode("frontend code");
        output.setTestCode("test code");
        output.setFolderStructure("folder structure");
        return output;
    }

    @Test
    void startGenerationJob_shouldSaveJobAndTriggerAsyncProcess() {
        SpecRequest specRequestDto = createValidSpecRequestDto();
        com.app.model.SpecRequest specRequestEntity = createValidSpecRequestEntity();
        UUID jobId = UUID.randomUUID();
        GenerationJob job = createGenerationJob(jobId, JobStatus.PENDING, specRequestEntity);

        when(generationJobRepository.save(any(GenerationJob.class))).thenReturn(job);

        when(generationJobRepository.findById(jobId)).thenReturn(Optional.of(job));

        when(aiServiceFactory.getAIService(anyBoolean())).thenReturn(aiService);
        when(specRequestMapper.toDto(any(com.app.model.SpecRequest.class))).thenReturn(specRequestDto);
        try {
            when(aiService.generate(specRequestDto)).thenReturn("{\"backendCode\":\"backend code\"}");
            GeneratedOutput generatedOutput = createGeneratedOutput(jobId, job);
            when(objectMapper.readValue(anyString(), eq(GeneratedOutput.class))).thenReturn(generatedOutput);
            when(generatedOutputRepository.save(any(GeneratedOutput.class))).thenReturn(generatedOutput);
        } catch (JsonProcessingException | AICustomException e) {
            fail("Mocking setup failed: " + e.getMessage());
        }


        JobSubmissionResponse response = generationService.startGenerationJob(specRequestDto);

        assertNotNull(response);
        assertEquals(jobId, response.getJobId());
    }

    @Test
    void processGeneration_shouldCompleteSuccessfully() throws JsonProcessingException, AICustomException {
        UUID jobId = UUID.randomUUID();
        com.app.model.SpecRequest specRequestEntity = createValidSpecRequestEntity();
        SpecRequest specRequestDto = createValidSpecRequestDto();
        GenerationJob job = createGenerationJob(jobId, JobStatus.PENDING, specRequestEntity);
        GeneratedOutput generatedOutput = createGeneratedOutput(jobId, job);

        when(generationJobRepository.findById(jobId)).thenReturn(Optional.of(job));
        when(generationJobRepository.save(any(GenerationJob.class))).thenReturn(job);
        when(specRequestMapper.toDto(specRequestEntity)).thenReturn(specRequestDto);
        when(aiService.generate(specRequestDto)).thenReturn("{\"backendCode\":\"backend code\"}");
        when(objectMapper.readValue(anyString(), eq(GeneratedOutput.class))).thenReturn(generatedOutput);
        when(generatedOutputRepository.save(any(GeneratedOutput.class))).thenReturn(generatedOutput);

        proxiedGenerationService.processGeneration(jobId);

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(generatedOutputRepository, atLeast(1)).save(any(GeneratedOutput.class));
            assertEquals(JobStatus.COMPLETE, job.getStatus());
            assertNull(job.getErrorMessage());
        });
    }

    @Test
    void processGeneration_shouldFailOnAICustomException() throws AICustomException {
        UUID jobId = UUID.randomUUID();
        com.app.model.SpecRequest specRequestEntity = createValidSpecRequestEntity();
        SpecRequest specRequestDto = createValidSpecRequestDto();
        GenerationJob job = createGenerationJob(jobId, JobStatus.PENDING, specRequestEntity);

        when(generationJobRepository.findById(jobId)).thenReturn(Optional.of(job));
        when(generationJobRepository.save(any(GenerationJob.class))).thenReturn(job);
        when(specRequestMapper.toDto(specRequestEntity)).thenReturn(specRequestDto);
        when(aiService.generate(specRequestDto)).thenThrow(new AICustomException("AI error"));

        proxiedGenerationService.processGeneration(jobId);

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(generatedOutputRepository, never()).save(any(GeneratedOutput.class));
            assertEquals(JobStatus.FAILED, job.getStatus());
            assertTrue(job.getErrorMessage().startsWith(AI_SERVICE_ERROR));
        });
    }

    @Test
    void processGeneration_shouldFailOnJsonProcessingException() throws JsonProcessingException, AICustomException {
        UUID jobId = UUID.randomUUID();
        com.app.model.SpecRequest specRequestEntity = createValidSpecRequestEntity();
        SpecRequest specRequestDto = createValidSpecRequestDto();
        GenerationJob job = createGenerationJob(jobId, JobStatus.PENDING, specRequestEntity);

        when(generationJobRepository.findById(jobId)).thenReturn(Optional.of(job));
        when(generationJobRepository.save(any(GenerationJob.class))).thenReturn(job);
        when(specRequestMapper.toDto(specRequestEntity)).thenReturn(specRequestDto);
        when(aiService.generate(specRequestDto)).thenReturn("invalid json");
        when(objectMapper.readValue(anyString(), eq(GeneratedOutput.class))).thenThrow(mock(JsonProcessingException.class));

        proxiedGenerationService.processGeneration(jobId);

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(aiServiceFactory, times(1)).getAIService(job.getSpecRequest().isRealModel());
            verify(aiService, times(1)).generate(specRequestDto);
            verify(generatedOutputRepository, never()).save(any(GeneratedOutput.class));
            assertEquals(JobStatus.FAILED, job.getStatus());
            assertTrue(job.getErrorMessage().startsWith(RESPONSE_PARSING_ERROR));
        });
    }

    @Test
    void processGeneration_shouldHandleJobNotFound() {
        UUID jobId = UUID.randomUUID();
        when(generationJobRepository.findById(jobId)).thenReturn(Optional.empty());

        proxiedGenerationService.processGeneration(jobId);

        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(generationJobRepository, times(1)).findById(jobId);
            verify(generationJobRepository, never()).save(any(GenerationJob.class));
            verify(aiServiceFactory, never()).getAIService(anyBoolean());
            verify(generatedOutputRepository, never()).save(any(GeneratedOutput.class));
        });
    }

    @Test
    void getJobStatus_shouldReturnCorrectStatus() {
        UUID jobId = UUID.randomUUID();
        GenerationJob job = createGenerationJob(jobId, JobStatus.COMPLETE, createValidSpecRequestEntity());
        when(generationJobRepository.findById(jobId)).thenReturn(Optional.of(job));

        StatusResponse response = generationService.getJobStatus(jobId);

        assertNotNull(response);
        assertEquals(JobStatus.COMPLETE.name(), response.getStatus());
        assertNull(response.getErrorMessage());
        verify(generationJobRepository, times(1)).findById(jobId);
    }

    @Test
    void getJobStatus_shouldReturnJobNotFoundStatus() {
        UUID jobId = UUID.randomUUID();
        when(generationJobRepository.findById(jobId)).thenReturn(Optional.empty());

        StatusResponse response = generationService.getJobStatus(jobId);

        assertNotNull(response);
        assertEquals(JobStatus.FAILED.name(), response.getStatus());
        assertEquals(JOB_NOT_FOUND_STATUS, response.getErrorMessage());
        verify(generationJobRepository, times(1)).findById(jobId);
    }

    @Test
    void getGeneratedOutput_shouldReturnOutputWhenPresent() {
        UUID jobId = UUID.randomUUID();
        GenerationJob job = createGenerationJob(jobId, JobStatus.COMPLETE, createValidSpecRequestEntity());
        GeneratedOutput output = createGeneratedOutput(jobId, job);
        when(generatedOutputRepository.findById(jobId)).thenReturn(Optional.of(output));

        Optional<GeneratedOutput> result = generationService.getGeneratedOutput(jobId);

        assertTrue(result.isPresent());
        assertEquals(output, result.get());
        verify(generatedOutputRepository, times(1)).findById(jobId);
    }

    @Test
    void getGeneratedOutput_shouldReturnEmptyWhenNotPresent() {
        UUID jobId = UUID.randomUUID();

        when(generatedOutputRepository.findById(jobId)).thenReturn(Optional.empty());

        Optional<GeneratedOutput> result = generationService.getGeneratedOutput(jobId);

        assertFalse(result.isPresent());
        verify(generatedOutputRepository, times(1)).findById(jobId);
    }

}
