package com.student.reservationservice.repository;

import com.student.reservationservice.entity.VisitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface VisitRepository extends JpaRepository<VisitEntity, Long>, JpaSpecificationExecutor<VisitEntity> {
    Optional<VisitEntity> findVisitById(Long id);

    @Query("SELECT v FROM VisitEntity v JOIN v.visitPositions vp WHERE DATE(v.startDate) = DATE(:startDate) AND vp.serviceTypeId IN :serviceTypeIds")
    List<VisitEntity> findByStartDateIgnoringTimeAndServiceTypeIds(Timestamp startDate, List<Long> serviceTypeIds);

    List<VisitEntity> findByApproved(boolean isApproved);

    List<VisitEntity> findVisitsByPatientIdAndApproved(Long patientId, boolean isApproved);

    void deleteVisitById(Long id);
}
