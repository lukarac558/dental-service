package com.student.reservationservice.controller;

import com.student.api.dto.reservation.CalendarDayDto;
import com.student.api.exception.NotFoundException;
import io.restassured.RestAssured;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CalendarDayControllerTest {
    @LocalServerPort
    private Integer port;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:16-alpine"
    );

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    CalendarDayController calendarDayController;
    static final Long EXISTING_DOCTOR_ID = 4444L;
    static final Long EXISTING_CALENDAR_DAY_ID = 8888L;
    static final Long EXISTING_ANOTHER_CALENDAR_DAY_ID = 8899L;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

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

//    @Transactional
//    @Test
//    void createCalendarDay_shouldCreate() {
//        CalendarDayDto day = getCalendarDay();
//        ResponseEntity<CalendarDayDto> dayResponse = calendarDayController.createCalendarDay(day);
//
//        int statusCode = dayResponse.getStatusCode().value();
//        CalendarDayDto calendarDay = Objects.requireNonNull(dayResponse.getBody());
//
//        assertEquals(HttpStatus.CREATED.value(), statusCode);
//        assertNotNull(calendarDay.getId());
//        assertEquals("2023-12-24 08:00:00", calendarDay.getStartDate());
//        assertEquals("04:00:00", calendarDay.getWorkDuration());
//        assertEquals(EXISTING_DOCTOR_ID, calendarDay.getDoctorId());
//    }

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

