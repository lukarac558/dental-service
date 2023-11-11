package com.student.reservationservice.repository;

import com.student.reservationservice.entity.CalendarDayEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CalendarDayRepository extends JpaRepository<CalendarDayEntity, Long> {
    Optional<CalendarDayEntity> findCalendarDayById(Long id);

    List<CalendarDayEntity> findCalendarDaysByDoctorId(Long userId);

    void deleteCalendarDayById(Long id);
}
