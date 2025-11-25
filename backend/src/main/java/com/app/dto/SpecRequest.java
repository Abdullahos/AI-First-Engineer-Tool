package com.app.dto;

import com.app.validation.ListElementsSize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SpecRequest {

    @NotBlank
    @Size(max = 2000)
    private String systemDescription;

    @NotEmpty
    @Size(max = 50)
    @ListElementsSize(max = 1000)
    private List<String> functionalRequirements;

    @NotEmpty
    @Size(max = 50)
    @ListElementsSize(max = 1000)
    private List<String> nonFunctionalRequirements;

    @NotBlank
    @Size(max = 5000)
    private String dataModel;

    @NotBlank
    @Size(max = 5000)
    private String apiDesign;

    @NotEmpty
    @Size(max = 50)
    @ListElementsSize(max = 1000)
    private List<String> acceptanceCriteria;

    private boolean isRealModel;
}
