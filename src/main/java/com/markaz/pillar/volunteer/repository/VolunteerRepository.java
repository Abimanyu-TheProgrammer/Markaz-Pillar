package com.markaz.pillar.volunteer.repository;

import com.markaz.pillar.volunteer.repository.model.VolunteerRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VolunteerRepository extends JpaRepository<VolunteerRegistration, Integer> {
}
