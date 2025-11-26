package com.app.controller;

import com.app.dto.JobSubmissionResponse;
import com.app.dto.SpecRequest;
import com.app.dto.StatusResponse;
import com.app.model.GeneratedOutput;
import com.app.service.GenerationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class GenerationController {

    private final GenerationService generationService;

    public GenerationController(GenerationService generationService) {
        this.generationService = generationService;
    }

    @PostMapping(value = "/generate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JobSubmissionResponse> generateCode(@Valid @RequestBody SpecRequest specRequest) {
        JobSubmissionResponse response = generationService.startGenerationJob(specRequest);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "/status/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StatusResponse> getStatus(@PathVariable("id") UUID id) {
        StatusResponse statusResponse = generationService.getJobStatus(id);
        return ResponseEntity.ok(statusResponse);
    }

    @GetMapping(value = "/result/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeneratedOutput> getResult(@PathVariable("id") UUID id) {
        return generationService.getGeneratedOutput(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
