package com.student.locationservice.repository;

import com.student.locationservice.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface CityRepository extends JpaRepository<CityEntity, Long>, JpaSpecificationExecutor<CityEntity> {
}
