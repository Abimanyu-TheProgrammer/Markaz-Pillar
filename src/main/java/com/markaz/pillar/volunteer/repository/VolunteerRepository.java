package com.markaz.pillar.volunteer.repository;

import com.markaz.pillar.auth.repository.models.AuthUser;
import com.markaz.pillar.volunteer.repository.model.VolunteerRegistration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VolunteerRepository extends
        JpaRepository<VolunteerRegistration, Integer>, JpaSpecificationExecutor<VolunteerRegistration> {
    Optional<VolunteerRegistration> getById(int id);

    Page<VolunteerRegistration> findAllByUser(AuthUser user, Pageable pageable);
}
