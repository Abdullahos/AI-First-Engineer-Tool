package com.app.service.aifactory;

import org.springframework.stereotype.Component;

@Component
public class AIServiceFactory {

    private final RealAIService realAIService;
    private final MockAIService mockAIService;

    public AIServiceFactory(RealAIService realAIService, MockAIService mockAIService) {
        this.realAIService = realAIService;
        this.mockAIService = mockAIService;
    }

    public AIService getAIService(boolean isRealModel) {
        if (isRealModel) {
            return realAIService;
        } else {
            return mockAIService;
        }
    }
}
