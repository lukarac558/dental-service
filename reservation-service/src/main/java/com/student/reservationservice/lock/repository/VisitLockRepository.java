package com.student.reservationservice.lock.repository;

import com.student.reservationservice.lock.entity.VisitLock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface VisitLockRepository extends JpaRepository<VisitLock, Long> {
        List<VisitLock> findVisitLocksByDoctorIdAndStartDateBetween(Long doctorId, Timestamp startRange, Timestamp endRange);

        void deleteVisitLockById(Long id);
}
