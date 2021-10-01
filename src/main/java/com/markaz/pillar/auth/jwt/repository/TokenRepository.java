package com.markaz.pillar.auth.jwt.repository;

import com.markaz.pillar.auth.jwt.repository.model.AuthRefresh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<AuthRefresh, Integer> {
    Optional<AuthRefresh> findByRefreshToken(String refreshToken);

    @Transactional
    @Modifying
    @Query("update AuthRefresh set isValid = false where email = ?1")
    void setAllValidFalseForUsername(String username);
}
