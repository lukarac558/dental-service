package com.student.reservationservice.controller;

import com.student.api.dto.reservation.CalendarDayDto;
import com.student.api.dto.reservation.VisitPositionDto;
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

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VisitPositionControllerTest {
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
    VisitPositionController visitPositionController;
    static final Long EXISTING_VISIT_POSITION_ID = 9999L;
    static final Long EXISTING_PATIENT_ID = 4455L;
    static final Long EXISTING_VISIT_ID = 1111L;
    static final Long EXISTING_SERVICE_TYPE_ID = 6666L;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    void getVisitPositionById_shouldFind() {
        ResponseEntity<VisitPositionDto> visitPositionResponse = visitPositionController.getVisitPositionById(EXISTING_VISIT_POSITION_ID);

        int statusCode = visitPositionResponse.getStatusCode().value();
        VisitPositionDto visitPosition = Objects.requireNonNull(visitPositionResponse.getBody());

        assertEquals(HttpStatus.OK.value(), statusCode);
        assertEquals(EXISTING_VISIT_POSITION_ID, visitPosition.getId());
        assertEquals(EXISTING_VISIT_ID, visitPosition.getVisitId());
        assertEquals(EXISTING_SERVICE_TYPE_ID, visitPosition.getServiceTypeId());
    }

    @Test
    void getVisitPositionById_shouldNotFind_throwNotFoundException() {
        assertThrows(NotFoundException.class, () -> visitPositionController.getVisitPositionById(7777L));
    }

    @Test
    void getVisitPositionsByVisitId_shouldFindTwo() {
        ResponseEntity<List<VisitPositionDto>> visitPositionResponse = visitPositionController.getVisitPositionsByVisitId(EXISTING_VISIT_ID);

        int statusCode = visitPositionResponse.getStatusCode().value();
        List<VisitPositionDto> visitPositions = Objects.requireNonNull(visitPositionResponse.getBody());

        assertEquals(HttpStatus.OK.value(), statusCode);
        assertEquals(2, visitPositions.size());
    }

//    @Transactional
//    @Test
//    void createVisitPosition_shouldCreate() {
//        Long serviceTypeId = 6677L;
//        ResponseEntity<VisitPositionDto> visitPositionResponse = visitPositionController.createVisitPosition(EXISTING_VISIT_ID, serviceTypeId);
//
//        int statusCode = visitPositionResponse.getStatusCode().value();
//        VisitPositionDto visitPosition = Objects.requireNonNull(visitPositionResponse.getBody());
//
//        assertEquals(HttpStatus.CREATED.value(), statusCode);
//        assertNotNull(visitPosition.getId());
//        assertEquals(EXISTING_VISIT_ID, visitPosition.getVisitId());
//        assertEquals(serviceTypeId, visitPosition.getServiceTypeId());
//    }

    @Transactional
    @Test
    void deleteVisitPosition_shouldDelete() {
        ResponseEntity<?> deleteResponse = visitPositionController.deleteVisitPosition(EXISTING_VISIT_POSITION_ID);

        int statusCode = deleteResponse.getStatusCode().value();

        assertEquals(HttpStatus.NO_CONTENT.value(), statusCode);
        assertThrows(NotFoundException.class, () -> visitPositionController.getVisitPositionById(EXISTING_VISIT_POSITION_ID));
    }
}

