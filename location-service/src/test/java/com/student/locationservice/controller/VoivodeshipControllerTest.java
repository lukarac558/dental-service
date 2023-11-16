package com.student.locationservice.controller;

import com.student.api.dto.location.VoivodeshipDto;
import com.student.api.exception.NotFoundException;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
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

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VoivodeshipControllerTest {
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
    VoivodeshipController voivodeshipController;
    static final Long EXISTING_VOIVODESHIP_ID = 77L;
    static final int TOTAL_VOIVODESHIPS_COUNT = 16;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    void getVoivodeshipById_shouldFind() {
        ResponseEntity<VoivodeshipDto> voivodeship = voivodeshipController.getVoivodeshipById(EXISTING_VOIVODESHIP_ID);

        int statusCode = voivodeship.getStatusCode().value();
        int id = Objects.requireNonNull(voivodeship.getBody()).getId();
        String name = Objects.requireNonNull(voivodeship.getBody()).getName();

        Assertions.assertEquals(HttpStatus.OK.value(), statusCode);
        Assertions.assertEquals(EXISTING_VOIVODESHIP_ID, id);
        Assertions.assertEquals("Kujawsko-Pomorskie", name);
    }

    @Test
    void getVoivodeshipById_shouldNotFind_throwNotFoundException() {
        assertThrows(NotFoundException.class, () -> voivodeshipController.getVoivodeshipById(9999L));
    }

    @Test
    void getAllVoivodeships_shouldFindSixteen() {
        ResponseEntity<List<VoivodeshipDto>> voivodeships = voivodeshipController.getAllVoivodeships();

        int statusCode = voivodeships.getStatusCode().value();
        int voivodeshipsCount = Objects.requireNonNull(voivodeships.getBody()).size();

        Assertions.assertEquals(HttpStatus.OK.value(), statusCode);
        Assertions.assertEquals(TOTAL_VOIVODESHIPS_COUNT, voivodeshipsCount);
    }
}
