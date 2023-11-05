package com.student.userservice.repository;

import com.student.api.dto.common.enums.Role;
import com.student.userservice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByIdAndRoles_Role(Long id, Role role);

    Optional<Void> deleteByEmail(String email);
}
