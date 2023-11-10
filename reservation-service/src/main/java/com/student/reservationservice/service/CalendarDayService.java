package com.student.reservationservice.service;

import com.student.reservationservice.entity.CalendarDay;
import com.student.reservationservice.repository.CalendarDayRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CalendarDayService {
    private final CalendarDayRepository calendarDayRepository;

    public CalendarDay addOrUpdateCalendarDay(CalendarDay calendarDay) {
        return calendarDayRepository.save(calendarDay);
    }

    public Optional<CalendarDay> findCalendarDayById(Long id) {
        return calendarDayRepository.findCalendarDayById(id);
    }

    public List<CalendarDay> findCalendarDaysByUserId(Long userId) {
        return calendarDayRepository.findCalendarDaysByDoctorId(userId);
    }

    @Transactional
    public void deleteCalendarDay(Long id) {
        calendarDayRepository.deleteCalendarDayById(id);
    }
}
