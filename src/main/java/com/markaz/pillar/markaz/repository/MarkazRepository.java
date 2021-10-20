package com.markaz.pillar.markaz.repository;

import com.markaz.pillar.markaz.repository.model.Markaz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MarkazRepository extends JpaRepository<Markaz, Integer>, JpaSpecificationExecutor<Markaz> {
    boolean existsByName(String name);
    boolean existsById(int id);

    Optional<Markaz> getBySlug(String slug);
    Optional<Markaz> getById(int id);
}
