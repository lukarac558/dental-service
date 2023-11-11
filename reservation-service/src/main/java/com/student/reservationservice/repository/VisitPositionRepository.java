package com.student.reservationservice.repository;

import com.student.reservationservice.entity.VisitPositionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VisitPositionRepository extends JpaRepository<VisitPositionEntity, Long> {
    Optional<VisitPositionEntity> findVisitPositionById(Long id);

    List<VisitPositionEntity> findVisitPositionsByVisitId(Long userId);

    void deleteVisitPositionById(Long id);
}
