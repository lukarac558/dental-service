package com.student.reservationservice.service;

import com.student.reservationservice.entity.CalendarDayEntity;
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

    public CalendarDayEntity addOrUpdateCalendarDay(CalendarDayEntity calendarDayEntity) {
        return calendarDayRepository.save(calendarDayEntity);
    }

    public Optional<CalendarDayEntity> findCalendarDayById(Long id) {
        return calendarDayRepository.findCalendarDayById(id);
    }

    public List<CalendarDayEntity> findCalendarDaysByDoctorId(Long userId) {
        return calendarDayRepository.findCalendarDaysByDoctorId(userId);
    }

    @Transactional
    public void deleteCalendarDay(Long id) {
        calendarDayRepository.deleteCalendarDayById(id);
    }
}
