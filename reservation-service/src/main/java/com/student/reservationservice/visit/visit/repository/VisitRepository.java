package com.student.reservationservice.visit.visit.repository;

import com.student.reservationservice.visit.visit.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface VisitRepository extends JpaRepository<Visit, Long> {
    Optional<Visit> findVisitById(Long id);

    Optional<Visit> findVisitsByStartDate(Timestamp startDate);

    List<Visit> findVisitsByPatientId(Long userId);

    void deleteVisitById(Long id);
}
