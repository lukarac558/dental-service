package com.student.userservice.controller;

import com.student.api.annotation.extractor.auth.Info;
import com.student.api.dto.common.enums.Role;
import com.student.api.dto.user.ServiceTypeDto;
import com.student.api.dto.user.ServiceTypeSearchRequestDto;
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
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TypeControllerTest {
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
    TypeController typeController;
    static final Long EXISTING_DOCTOR_ID = 4444L;
    static final Long EXISTING_TYPE_ID = 6666L;
    static final Long EXISTING_ANOTHER_TYPE_ID = 6677L;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    void getTypeById_shouldFind() {
        ResponseEntity<ServiceTypeDto> typeResponse = typeController.getTypeById(EXISTING_TYPE_ID);

        int statusCode = typeResponse.getStatusCode().value();
        ServiceTypeDto type = Objects.requireNonNull(typeResponse.getBody());

        assertEquals(HttpStatus.OK.value(), statusCode);
        assertEquals(EXISTING_TYPE_ID, type.getId());
        assertEquals("Plombowanie", type.getName());
        assertEquals("01:00:00", type.getDurationTime());
        assertEquals(EXISTING_DOCTOR_ID, type.getDoctorId());
        assertEquals("Opis uslugi plombowania zeba.", type.getDescription());
        assertTrue(type.getEnabled());
    }

    @Test
    void getTypeById_shouldNotFind_throwNotFoundException() {
        assertThrows(NotFoundException.class, () -> typeController.getTypeById(9999L));
    }

    @Test
    void getTypesByIds_shouldFindBoth() {
        List<Long> typeIds = List.of(EXISTING_TYPE_ID, EXISTING_ANOTHER_TYPE_ID);
        ResponseEntity<List<ServiceTypeDto>> typesResponse = typeController.getTypesByIds(typeIds);

        int statusCode = typesResponse.getStatusCode().value();
        List<ServiceTypeDto> types = Objects.requireNonNull(typesResponse.getBody());
        List<Long> responseTypeIds = types.stream().map(ServiceTypeDto::getId).toList();

        assertEquals(HttpStatus.OK.value(), statusCode);
        assertEquals(2, types.size());
        assertTrue(responseTypeIds.containsAll(typeIds));
    }

    @Test
    void getTypesByRequest_shouldFindOne() {
        Info info = new Info("lukasz.raczka@gmail.com", List.of(Role.DOCTOR));
        ServiceTypeSearchRequestDto searchRequest = getTypeSearchRequest();
        ResponseEntity<Page<ServiceTypeDto>> typesResponse = typeController.getTypesByRequest(info, searchRequest);

        int statusCode = typesResponse.getStatusCode().value();
        List<ServiceTypeDto> types = Objects.requireNonNull(typesResponse.getBody()).getContent();

        assertEquals(HttpStatus.OK.value(), statusCode);
        assertEquals(1, types.size());
        assertEquals("Wyrywanie", types.get(0).getName());
        assertEquals(EXISTING_DOCTOR_ID, types.get(0).getDoctorId());
    }

    @Transactional
    @Test
    void createType_shouldCreate() {
        Info info = new Info("lukasz.raczka@gmail.com", List.of(Role.DOCTOR));
        ServiceTypeDto type = getServiceType();
        ResponseEntity<ServiceTypeDto> typeResponse = typeController.createServiceType(info, type);

        int statusCode = typeResponse.getStatusCode().value();
        ServiceTypeDto serviceType = Objects.requireNonNull(typeResponse.getBody());

        assertEquals(HttpStatus.CREATED.value(), statusCode);
        assertNotNull(serviceType.getId());
        assertEquals("Wizyta kontrolna", serviceType.getName());
        assertEquals("01:00:00", serviceType.getDurationTime());
        assertEquals(EXISTING_DOCTOR_ID, serviceType.getDoctorId());
        assertEquals("Testowy opis.", serviceType.getDescription());
        assertTrue(serviceType.getEnabled());
    }

    @Test
    void updateType_shouldUpdate() {
        Info info = new Info("lukasz.raczka@gmail.com", List.of(Role.DOCTOR));
        ServiceTypeDto type = getServiceType();
        ResponseEntity<ServiceTypeDto> typeResponse = typeController.updateType(info, EXISTING_TYPE_ID, type);

        int statusCode = typeResponse.getStatusCode().value();
        ServiceTypeDto serviceType = Objects.requireNonNull(typeResponse.getBody());

        assertEquals(HttpStatus.OK.value(), statusCode);
        assertEquals("Wizyta kontrolna", serviceType.getName());
        assertEquals(EXISTING_DOCTOR_ID, serviceType.getDoctorId());
        assertEquals("Testowy opis.", serviceType.getDescription());
        assertTrue(serviceType.getEnabled());
    }

    @Transactional
    @Test
    void deleteType_shouldDelete() {
        Info info = new Info("lukasz.raczka@gmail.com", List.of(Role.DOCTOR));
        ResponseEntity<Void> deleteResponse = typeController.deleteType(info, EXISTING_TYPE_ID);

        int statusCode = deleteResponse.getStatusCode().value();
        ResponseEntity<ServiceTypeDto> typeResponse = typeController.getTypeById(EXISTING_TYPE_ID);
        ServiceTypeDto type = Objects.requireNonNull(typeResponse.getBody());

        assertEquals(HttpStatus.NO_CONTENT.value(), statusCode);
        assertFalse(type.getEnabled());
    }

    private ServiceTypeDto getServiceType() {
        ServiceTypeDto type = new ServiceTypeDto();
        type.setName("Wizyta kontrolna");
        type.setDurationTime("1:00:00");
        type.setDoctorId(EXISTING_DOCTOR_ID);
        type.setDescription("Testowy opis.");
        return type;
    }

    private ServiceTypeSearchRequestDto getTypeSearchRequest() {
        ServiceTypeSearchRequestDto searchRequest = new ServiceTypeSearchRequestDto();
        searchRequest.setName("Wyrywanie");
        searchRequest.setShowOnlyYour(true);
        return searchRequest;
    }
}

