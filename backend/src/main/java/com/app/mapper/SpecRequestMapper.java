package com.app.mapper;

import com.app.dto.SpecRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SpecRequestMapper {
    com.app.model.SpecRequest toEntity(SpecRequest dto);
    SpecRequest toDto(com.app.model.SpecRequest entity);
}
