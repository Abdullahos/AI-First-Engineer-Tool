package com.app.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Embeddable
@Getter
@Setter
public class SpecRequest {

    @Lob
    @Column(name = "system_description", nullable = false)
    private String systemDescription;

    @ElementCollection
    @CollectionTable(name = "functional_requirements", joinColumns = @JoinColumn(name = "job_id"))
    @Column(name = "requirement", nullable = false)
    private List<String> functionalRequirements;

    @ElementCollection
    @CollectionTable(name = "non_functional_requirements", joinColumns = @JoinColumn(name = "job_id"))
    @Column(name = "requirement", nullable = false)
    private List<String> nonFunctionalRequirements;

    @Lob
    @Column(name = "data_model", nullable = false)
    private String dataModel;

    @Lob
    @Column(name = "api_design", nullable = false)
    private String apiDesign;

    @ElementCollection
    @CollectionTable(name = "acceptance_criteria", joinColumns = @JoinColumn(name = "job_id"))
    @Column(name = "criterion", nullable = false)
    private List<String> acceptanceCriteria;

    @Column(name = "is_real_model", nullable = false)
    private boolean isRealModel;
}
