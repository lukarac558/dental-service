package com.student.reservationservice.controller;

import com.student.api.dto.reservation.VisitDto;
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

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VisitControllerTest {
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
    VisitController visitController;
    static final Long EXISTING_VISIT_ID = 1111L;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    void getVisitById_shouldNotFind_throwNotFoundException() {
        assertThrows(NotFoundException.class, () -> visitController.getVisitById(9999L));
    }

    @Transactional
    @Test
    void updateVisitDescription_shouldUpdate() {
        String description = "Nowy opis";
        ResponseEntity<VisitDto> visitResponse = visitController.updateVisitDescription(EXISTING_VISIT_ID, description);

        int statusCode = visitResponse.getStatusCode().value();
        VisitDto visit = Objects.requireNonNull(visitResponse.getBody());

        assertEquals(HttpStatus.OK.value(), statusCode);
        assertEquals(EXISTING_VISIT_ID, visit.getId());
        assertEquals(description, visit.getDescription());
    }

    @Transactional
    @Test
    void deleteVisit_shouldDelete() {
        ResponseEntity<?> deleteResponse = visitController.deleteVisitById(EXISTING_VISIT_ID);

        int statusCode = deleteResponse.getStatusCode().value();

        assertEquals(HttpStatus.NO_CONTENT.value(), statusCode);
        assertThrows(NotFoundException.class, () -> visitController.getVisitById(EXISTING_VISIT_ID));
    }
}

