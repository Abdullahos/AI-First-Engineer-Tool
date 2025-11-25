package com.app.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class JobSubmissionResponse {
    private UUID jobId;

    public JobSubmissionResponse(UUID jobId) {
        this.jobId = jobId;
    }
}
