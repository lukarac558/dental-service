package com.student.reservationservice.servicetype.repository;

import com.student.reservationservice.servicetype.entity.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServiceTypeRepository extends JpaRepository<ServiceType, Long> {
    Optional<ServiceType> findServiceTypeById(Long id);
    List<ServiceType> findServiceTypesByUserId(Long id);
    void deleteServiceTypeById(Long id);
}