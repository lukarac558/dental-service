package com.student.reservationservice.repository;

import com.student.reservationservice.entity.VisitLockEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface VisitLockRepository extends JpaRepository<VisitLockEntity, Long> {
    List<VisitLockEntity> findVisitLockEntitiesByDoctorIdAndStartDate(Long doctorId, Timestamp startDate);

    @Transactional
    void deleteVisitLockEntitiesByDoctorIdAndStartDate(Long doctorId, Timestamp startDate);

    @Transactional
    void deleteVisitLockEntityById(Long id);
}
