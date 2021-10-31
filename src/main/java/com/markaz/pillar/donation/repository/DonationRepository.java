package com.markaz.pillar.donation.repository;

import com.markaz.pillar.donation.repository.model.DonationDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DonationRepository extends JpaRepository<DonationDetail, Integer>, JpaSpecificationExecutor<DonationDetail> {
    Optional<DonationDetail> getByUniqueId(String id);
}
