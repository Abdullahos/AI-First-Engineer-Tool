package com.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "generated_outputs")
@Getter
@Setter
public class GeneratedOutput {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private GenerationJob generationJob;

    @Lob
    @Column(name = "backend_code")
    private String backendCode;

    @Lob
    @Column(name = "frontend_code")
    private String frontendCode;

    @Lob
    @Column(name = "test_code")
    private String testCode;

    @Lob
    @Column(name = "folder_structure")
    private String folderStructure;
}
