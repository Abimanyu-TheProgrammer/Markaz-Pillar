package com.markaz.pillar.transaction.repository;

import com.markaz.pillar.auth.repository.models.AuthUser;
import com.markaz.pillar.markaz.repository.model.Markaz;
import com.markaz.pillar.santri.repository.model.Santri;
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
    Page<UserTransaction> findAllByDonationDetail_Markaz(Markaz markaz, Pageable pageable);
    Page<UserTransaction> findAllByDonationDetail_Santri(Santri santri, Pageable pageable);
}
