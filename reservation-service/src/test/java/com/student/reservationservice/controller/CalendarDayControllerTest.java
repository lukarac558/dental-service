package com.student.reservationservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.student.api.dto.reservation.CalendarDayDto;
import com.student.api.dto.user.UserPersonalDetailsDto;
import com.student.api.exception.NotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.junit.jupiter.api.Assertions.*;

class CalendarDayControllerTest extends BaseControllerTests {
    @Autowired
    CalendarDayController calendarDayController;
    static final Long EXISTING_DOCTOR_ID = 4444L;
    static final Long EXISTING_CALENDAR_DAY_ID = 8888L;
    static final Long EXISTING_ANOTHER_CALENDAR_DAY_ID = 8899L;

    @Test
    void getCalendarDayById_shouldFind() {
        ResponseEntity<CalendarDayDto> day = calendarDayController.getCalendarDayById(EXISTING_CALENDAR_DAY_ID);

        int statusCode = day.getStatusCode().value();
        CalendarDayDto calendarDay = Objects.requireNonNull(day.getBody());

        assertEquals(HttpStatus.OK.value(), statusCode);
        assertEquals(EXISTING_CALENDAR_DAY_ID, calendarDay.getId());
        assertEquals("2024-03-18 07:00:00.0", calendarDay.getStartDate());
        assertEquals("07:00:00", calendarDay.getWorkDuration());
        assertEquals(4444, calendarDay.getDoctorId());
    }

    @Test
    void getCalendarDayById_shouldNotFind_throwNotFoundException() {
        assertThrows(NotFoundException.class, () -> calendarDayController.getCalendarDayById(9999L));
    }

    @Test
    void getCalendarDaysByDoctorId_shouldFindBoth() {
        ResponseEntity<List<CalendarDayDto>> daysResponse = calendarDayController.getCalendarDaysByDoctorId(EXISTING_DOCTOR_ID);

        int statusCode = daysResponse.getStatusCode().value();
        List<CalendarDayDto> days = Objects.requireNonNull(daysResponse.getBody());

        assertEquals(HttpStatus.OK.value(), statusCode);
        assertEquals(2, days.size());
    }

    @Transactional
    @Test
    void createCalendarDay_shouldCreate() throws JsonProcessingException {
        server.stubFor(WireMock
                .get(String.format("/users/%s", EXISTING_DOCTOR_ID))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(new ObjectMapper().writeValueAsString(new UserPersonalDetailsDto()))
                        .withStatus(HttpStatus.OK.value())
                )
        );

        CalendarDayDto day = getCalendarDay();
        ResponseEntity<CalendarDayDto> dayResponse = calendarDayController.createCalendarDay(day);

        int statusCode = dayResponse.getStatusCode().value();
        CalendarDayDto calendarDay = Objects.requireNonNull(dayResponse.getBody());

        assertEquals(HttpStatus.CREATED.value(), statusCode);
        assertNotNull(calendarDay.getId());
        assertEquals("2023-12-24 08:00:00", calendarDay.getStartDate());
        assertEquals("04:00:00", calendarDay.getWorkDuration());
        assertEquals(EXISTING_DOCTOR_ID, calendarDay.getDoctorId());
    }

    @Transactional
    @Test
    void deleteCalendarDay_shouldDelete() {
        ResponseEntity<?> deleteResponse = calendarDayController.deleteCalendarDay(EXISTING_CALENDAR_DAY_ID);

        int statusCode = deleteResponse.getStatusCode().value();

        assertEquals(HttpStatus.NO_CONTENT.value(), statusCode);
        assertThrows(NotFoundException.class, () -> calendarDayController.getCalendarDayById(EXISTING_CALENDAR_DAY_ID));
    }

    private CalendarDayDto getCalendarDay() {
        CalendarDayDto day = new CalendarDayDto();
        day.setStartDate("2023-12-24 08:00:00");
        day.setWorkDuration("04:00");
        day.setDoctorId(EXISTING_DOCTOR_ID);
        return day;
    }
}

