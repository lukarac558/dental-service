package com.student.reservationservice.repository;

import com.student.reservationservice.entity.CalendarDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CalendarDayRepository extends JpaRepository<CalendarDay, Long> {
    Optional<CalendarDay> findCalendarDayById(Long id);

    List<CalendarDay> findCalendarDaysByDoctorId(Long userId);

    void deleteCalendarDayById(Long id);
}
