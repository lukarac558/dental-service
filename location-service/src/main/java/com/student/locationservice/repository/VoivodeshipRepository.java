package com.student.locationservice.repository;

import com.student.locationservice.entity.VoivodeshipEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoivodeshipRepository extends JpaRepository<VoivodeshipEntity, Long> {
}
