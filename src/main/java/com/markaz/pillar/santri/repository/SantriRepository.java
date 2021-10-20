package com.markaz.pillar.santri.repository;

import com.markaz.pillar.santri.repository.model.Santri;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SantriRepository extends JpaRepository<Santri, Integer>, JpaSpecificationExecutor<Santri> {
    boolean existsByNameAndMarkaz_Id(String name, int markazId);
    boolean existsById(int id);

    Optional<Santri> getBySlug(String slug);
    Optional<Santri> getById(int id);
}
