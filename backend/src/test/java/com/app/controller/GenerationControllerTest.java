package com.app.controller;

import com.app.dto.JobSubmissionResponse;
import com.app.dto.SpecRequest;
import com.app.dto.StatusResponse;
import com.app.model.GeneratedOutput;
import com.app.model.JobStatus;
import com.app.service.GenerationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GenerationController.class)
class GenerationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GenerationService generationService;

    private SpecRequest createValidSpecRequest() {
        SpecRequest specRequest = new SpecRequest();
        specRequest.setSystemDescription("Valid system description");
        specRequest.setFunctionalRequirements(Arrays.asList("FR1", "FR2"));
        specRequest.setNonFunctionalRequirements(Arrays.asList("NFR1"));
        specRequest.setDataModel("Valid data model");
        specRequest.setApiDesign("Valid API design");
        specRequest.setAcceptanceCriteria(Arrays.asList("AC1"));
        specRequest.setRealModel(false);
        return specRequest;
    }

    @Test
    void generateCode_shouldReturn202AcceptedForValidRequest() throws Exception {
        SpecRequest validSpecRequest = createValidSpecRequest();
        UUID jobId = UUID.randomUUID();
        JobSubmissionResponse expectedResponse = new JobSubmissionResponse(jobId);

        when(generationService.startGenerationJob(any(SpecRequest.class))).thenReturn(expectedResponse);

        mockMvc.perform(post("/api/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validSpecRequest)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.jobId", is(jobId.toString())));
    }

    @Test
    void generateCode_shouldReturn400BadRequestForInvalidRequest() throws Exception {
        SpecRequest invalidSpecRequest = createValidSpecRequest();
        invalidSpecRequest.setSystemDescription(""); // Invalid: @NotBlank

        mockMvc.perform(post("/api/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidSpecRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.systemDescription", is("must not be blank")));
    }

    @Test
    void getStatus_shouldReturn200OkWithStatusResponse() throws Exception {
        UUID jobId = UUID.randomUUID();
        StatusResponse expectedResponse = new StatusResponse(JobStatus.COMPLETE.name(), null);

        when(generationService.getJobStatus(jobId)).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/status/{id}", jobId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(JobStatus.COMPLETE.name())))
                .andExpect(jsonPath("$.errorMessage").doesNotExist());
    }

    @Test
    void getStatus_shouldReturnJobNotFoundStatus() throws Exception {
        UUID jobId = UUID.randomUUID();
        StatusResponse expectedResponse = new StatusResponse(JobStatus.FAILED.name(), "Job not found");

        when(generationService.getJobStatus(jobId)).thenReturn(expectedResponse);

        mockMvc.perform(get("/api/status/{id}", jobId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(JobStatus.FAILED.name())))
                .andExpect(jsonPath("$.errorMessage", is("Job not found")));
    }

    @Test
    void getResult_shouldReturn200OkWithGeneratedOutput() throws Exception {
        UUID jobId = UUID.randomUUID();
        GeneratedOutput expectedOutput = new GeneratedOutput();
        expectedOutput.setId(jobId);
        expectedOutput.setBackendCode("public class Test {}");
        expectedOutput.setFrontendCode("function Test() {}");

        when(generationService.getGeneratedOutput(jobId)).thenReturn(Optional.of(expectedOutput));

        mockMvc.perform(get("/api/result/{id}", jobId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(jobId.toString())))
                .andExpect(jsonPath("$.backendCode", is("public class Test {}")));
    }

    @Test
    void getResult_shouldReturn404NotFoundWhenOutputNotPresent() throws Exception {
        UUID jobId = UUID.randomUUID();

        when(generationService.getGeneratedOutput(jobId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/result/{id}", jobId))
                .andExpect(status().isNotFound());
    }
}
