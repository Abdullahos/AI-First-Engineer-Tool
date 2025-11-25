package com.app.model;

import com.app.config.TimestampedUuidGenerator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "generation_jobs")
@Getter
@Setter
public class GenerationJob {

    @Id
    @GeneratedValue(generator = "timestamped-uuid")
    @GenericGenerator(name = "timestamped-uuid", type = TimestampedUuidGenerator.class)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Embedded
    private SpecRequest specRequest;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private JobStatus status;

    @Lob
    @Column(name = "error_message")
    private String errorMessage;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
