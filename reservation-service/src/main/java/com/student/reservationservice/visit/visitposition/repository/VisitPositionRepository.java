package com.student.reservationservice.visit.visitposition.repository;

import com.student.reservationservice.visit.visitposition.entity.VisitPosition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VisitPositionRepository extends JpaRepository<VisitPosition, Long> {
    Optional<VisitPosition> findVisitPositionById(Long id);

    List<VisitPosition> findVisitPositionsByVisitId(Long userId);

    void deleteVisitPositionById(Long id);
}
