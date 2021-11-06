package com.markaz.pillar.auth.repository;

import com.markaz.pillar.auth.repository.models.AuthUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AuthUser, Integer> {
    Optional<AuthUser> findByEmail(String email);
    Optional<AuthUser> findByUsername(String username);

    Page<AuthUser> findAllByRoleName(String name, Pageable pageable);
}
