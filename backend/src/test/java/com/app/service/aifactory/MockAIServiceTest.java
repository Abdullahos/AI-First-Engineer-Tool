package com.app.service.aifactory;

import com.app.dto.SpecRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MockAIServiceTest {

    private MockAIService mockAIService;

    @BeforeEach
    void setUp() {
        mockAIService = new MockAIService();
    }

    @Test
    void generate_shouldReturnHardcodedJson() throws AICustomException {
        // Given
        SpecRequest specRequest = new SpecRequest(); // Content doesn't matter for mock

        // When
        String result = mockAIService.generate(specRequest);

        // Then
        assertNotNull(result);
        assertTrue(result.contains("\"backendCode\":"));
        assertTrue(result.contains("\"frontendCode\":"));
        assertTrue(result.contains("\"testCode\":"));
        assertTrue(result.contains("\"folderStructure\":"));
    }

    @Test
    void generate_shouldSimulateDelay() throws AICustomException {
        // Given
        SpecRequest specRequest = new SpecRequest();
        long startTime = System.currentTimeMillis();

        // When
        mockAIService.generate(specRequest);
        long endTime = System.currentTimeMillis();

        // Then
        long duration = endTime - startTime;
        assertTrue(duration >= 1000, "Expected a delay of at least 1000ms");
    }
}
