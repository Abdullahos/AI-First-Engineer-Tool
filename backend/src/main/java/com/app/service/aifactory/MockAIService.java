package com.app.service.aifactory;

import com.app.dto.SpecRequest;
import org.springframework.stereotype.Service;

@Service
public class MockAIService implements AIService {

    @Override
    public String generate(SpecRequest specRequest) throws AICustomException {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AICustomException("Mock AI service interrupted", e);
        }

        return """
                {
                    "backendCode": "package com.app.backend;\\n\\npublic class BackendService {\\n    public String getData() {\\n        return \\"Mock Backend Data\\";\\n    }\\n}",
                    "frontendCode": "import React from 'react';\\n\\nfunction FrontendComponent() {\\n    return <div>Mock Frontend UI</div>;\\n}\\nexport default FrontendComponent;",
                    "testCode": "import org.junit.jupiter.api.Test;\\nimport static org.junit.jupiter.api.Assertions.assertEquals;\\n\\npublic class MockBackendServiceTest {\\n    @Test\\n    void testGetData() {\\n        assertEquals(\\"Mock Backend Data\\", new BackendService().getData());\\n    }\\n}",
                    "folderStructure": "backend/\\n├── src/\\n│   └── main/\\n│       └── java/\\n│           └── com/\\n│               └── app/\\n│                   └── backend/\\n│                       └── BackendService.java\\nfrontend/\\n├── src/\\n│   └── components/\\n│       └── FrontendComponent.js\\ntests/\\n└── MockBackendServiceTest.java"
                }
                """;
    }
}
