package com.markaz.pillar.donation.repository;

import com.markaz.pillar.donation.repository.model.DonationDetail;
import com.markaz.pillar.markaz.repository.model.Markaz;
import com.markaz.pillar.santri.repository.model.Santri;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DonationRepository extends JpaRepository<DonationDetail, Integer>, JpaSpecificationExecutor<DonationDetail> {
    Optional<DonationDetail> getByUniqueId(String id);
    Page<DonationDetail> findAllByMarkaz(Markaz markaz, Specification<DonationDetail> specification, Pageable pageable);
    Page<DonationDetail> findAllBySantri(Santri santri, Specification<DonationDetail> specification, Pageable pageable);
}
