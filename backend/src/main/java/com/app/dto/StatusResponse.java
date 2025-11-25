package com.app.dto;

import lombok.Getter;
import lombok.Setter;

public class StatusResponse {
    private String status;
    private String errorMessage;

    public StatusResponse(String status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }
}
