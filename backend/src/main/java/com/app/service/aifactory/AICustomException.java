package com.app.service.aifactory;

public class AICustomException extends RuntimeException {
    public AICustomException(String message) {
        super(message);
    }

    public AICustomException(String message, Throwable cause) {
        super(message, cause);
    }
}
