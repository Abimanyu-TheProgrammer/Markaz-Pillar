package com.markaz.pillar.donation.repository;

import com.markaz.pillar.donation.repository.model.UserDonation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDonationRepository extends JpaRepository<UserDonation, Integer> {
}
