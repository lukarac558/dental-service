package com.student.userservice.repository;

import com.student.userservice.entity.CompetencyInformationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompetencyRepository extends JpaRepository<CompetencyInformationEntity, Long> {
    Optional<CompetencyInformationEntity> findByDoctor_Email(String id);
}
