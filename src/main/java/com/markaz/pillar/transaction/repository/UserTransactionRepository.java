package com.markaz.pillar.transaction.repository;

import com.markaz.pillar.auth.repository.models.AuthUser;
import com.markaz.pillar.donation.repository.model.DonationDetail;
import com.markaz.pillar.transaction.repository.model.UserTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTransactionRepository extends JpaRepository<UserTransaction, Integer> {
    Optional<UserTransaction> findByTrxId(String trxId);
    Page<UserTransaction> findAllByUser(AuthUser user, Pageable pageable);
    Page<UserTransaction> findAllByDonationDetail(DonationDetail donationDetail, Pageable pageable);
}
