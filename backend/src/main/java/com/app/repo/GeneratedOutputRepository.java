package com.app.repo;

import com.app.model.GeneratedOutput;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GeneratedOutputRepository extends JpaRepository<GeneratedOutput, UUID> {
}
