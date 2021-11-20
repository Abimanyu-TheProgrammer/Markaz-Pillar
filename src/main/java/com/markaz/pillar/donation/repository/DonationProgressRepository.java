package com.markaz.pillar.donation.repository;

import com.markaz.pillar.donation.repository.model.DonationProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DonationProgressRepository extends JpaRepository<DonationProgress, Integer> {
    Optional<DonationProgress> getById(int id);
}
