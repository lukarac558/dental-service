package com.student.reservationservice.user.applicationuser.repository;

import com.student.reservationservice.user.applicationuser.entity.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<ApplicationUser, Long> {
    Optional<ApplicationUser> findApplicationUserById(Long id);
    Optional<ApplicationUser> findApplicationUserByEmail(String email);
    Optional<ApplicationUser> findApplicationUserByEmailAndPassword(String email, String password);
    void deleteUserById(Long id);
}
