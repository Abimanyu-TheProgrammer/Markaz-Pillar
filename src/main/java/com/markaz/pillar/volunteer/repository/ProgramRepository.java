package com.markaz.pillar.volunteer.repository;

import com.markaz.pillar.volunteer.repository.model.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Integer> {
}
