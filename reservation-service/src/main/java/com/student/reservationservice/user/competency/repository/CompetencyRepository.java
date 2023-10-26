package com.student.reservationservice.user.competency.repository;

import com.student.reservationservice.user.competency.entity.CompetencyInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompetencyRepository extends JpaRepository<CompetencyInformation, Long> {
    Optional<CompetencyInformation> findCompetencyInformationById(Long id);
}
