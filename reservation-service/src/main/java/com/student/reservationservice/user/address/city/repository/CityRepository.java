package com.student.reservationservice.user.address.city.repository;

import com.student.reservationservice.user.address.city.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CityRepository extends JpaRepository<City, String> {
    Optional<City> findCityByCode(String code);

    List<City> findCitiesByVoivodeshipId(int voivodeshipId);
    void deleteCityByCode(String code);
}
