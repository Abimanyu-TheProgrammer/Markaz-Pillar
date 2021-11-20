package com.markaz.pillar.volunteer.repository;

import com.markaz.pillar.volunteer.repository.model.VolunteerProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProgramRepository extends
        JpaRepository<VolunteerProgram, Integer>, JpaSpecificationExecutor<VolunteerProgram> {
    boolean existsByName(String name);

    Optional<VolunteerProgram> getById(int id);
    Optional<VolunteerProgram> getBySlug(String slug);
}
