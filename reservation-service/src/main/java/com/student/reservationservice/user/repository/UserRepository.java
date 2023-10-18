package com.student.reservationservice.user.repository;

import com.student.reservationservice.user.entity.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<ApplicationUser, Long> {
    Optional<ApplicationUser> findUserById(Long id);
    void deleteUserById(Long id);
}
