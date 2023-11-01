package com.student.reservationservice.calendar.service;

import com.student.reservationservice.calendar.entity.CalendarDay;
import com.student.reservationservice.calendar.repository.CalendarDayRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CalendarDayService {
    private final CalendarDayRepository calendarDayRepository;

    @Autowired
    public CalendarDayService(CalendarDayRepository calendarDayRepository) {
        this.calendarDayRepository = calendarDayRepository;
    }

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
