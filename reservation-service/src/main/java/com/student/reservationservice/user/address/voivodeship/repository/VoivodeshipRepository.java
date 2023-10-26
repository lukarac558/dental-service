package com.student.reservationservice.user.address.voivodeship.repository;

import com.student.reservationservice.user.address.voivodeship.entity.Voivodeship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoivodeshipRepository extends JpaRepository<Voivodeship, Integer> {
    Optional<Voivodeship> findVoivodeshipByName(String name);

    Optional<Voivodeship> findVoivodeshipById(int id);

    void deleteVoivodeshipById(int id);
}
