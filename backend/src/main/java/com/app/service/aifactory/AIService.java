package com.app.service.aifactory;

import com.app.dto.SpecRequest;

public interface AIService {
    String generate(SpecRequest specRequest) throws AICustomException;
}
