package com.markaz.pillar.auth.google.repository;

import com.markaz.pillar.auth.google.repository.addon.GoogleTokenAddOn;
import com.markaz.pillar.auth.google.repository.model.GoogleToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface GoogleTokenRepository extends JpaRepository<GoogleToken, Integer>, GoogleTokenAddOn {
    @Transactional
    @Modifying
    @Query(value = "update google_token set is_active = false where user_id = ?1", nativeQuery = true)
    void deleteExistingTokens(int userId);

    GoogleToken findFirstByAccount_IdOrderByCreatedAtDesc(int id);
    GoogleToken getByState(String state);
}