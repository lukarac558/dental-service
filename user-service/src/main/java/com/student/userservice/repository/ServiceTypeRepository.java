package com.student.userservice.repository;

import com.student.userservice.entity.ServiceTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceTypeEntity, Long>, JpaSpecificationExecutor<ServiceTypeEntity> {
    Optional<ServiceTypeEntity> findByIdAndDoctor_Email(Long id, String email);
    void deleteByIdAndDoctor_Email(Long id, String email);
    List<ServiceTypeEntity> findServiceTypeEntitiesByDoctor_Id(Long doctorId);
}