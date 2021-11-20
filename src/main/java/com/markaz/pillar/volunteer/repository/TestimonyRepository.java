package com.markaz.pillar.volunteer.repository;

import com.markaz.pillar.volunteer.repository.model.ProgramTestimony;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestimonyRepository extends JpaRepository<ProgramTestimony, Integer> {
}
