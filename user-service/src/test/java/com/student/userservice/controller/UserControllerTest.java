package com.student.userservice.controller;

import com.student.api.annotation.extractor.auth.Info;
import com.student.api.dto.common.enums.Role;
import com.student.api.dto.common.enums.Sex;
import com.student.api.dto.location.VoivodeshipDto;
import com.student.api.dto.user.*;
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

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
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
    UserController userController;

    static final Long COMPETENCY_INFORMATION_ID = 2222L;
    static final Long EXISTING_DOCTOR_ID = 4444L;
    static final Long EXISTING_PATIENT_ADDRESS_ID = 3344L;
    static final Long EXISTING_PATIENT_ID = 4455L;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Transactional
    @Test
    void getUserById_shouldFind() {
        ResponseEntity<UserPersonalDetailsDto> userResponse = userController.getUserById(EXISTING_PATIENT_ID);
        int statusCode = userResponse.getStatusCode().value();

        UserPersonalDetailsDto user = Objects.requireNonNull(userResponse.getBody());
        AddressDto address = user.getAddress();

        assertEquals(HttpStatus.OK.value(), statusCode);
        assertEquals(EXISTING_PATIENT_ID, user.getId());
        assertEquals("pum3xlr@gmail.com", user.getEmail());
        assertEquals("77011779469", user.getPersonalId());
        assertEquals("Andrzej", user.getName());
        assertEquals("Rączka", user.getSurname());
        assertEquals("946582113", user.getPhoneNumber());
        assertEquals(EXISTING_PATIENT_ADDRESS_ID, address.getId());
    }

    @Transactional
    @Test
    void getUserByEmail_shouldFind() {
        Info info = new Info("pum3xlr@gmail.com", List.of(Role.PATIENT));
        ResponseEntity<UserPersonalDetailsDto> userResponse = userController.getUser(info);
        int statusCode = userResponse.getStatusCode().value();

        UserPersonalDetailsDto user = Objects.requireNonNull(userResponse.getBody());
        AddressDto address = user.getAddress();

        assertEquals(HttpStatus.OK.value(), statusCode);
        assertEquals(EXISTING_PATIENT_ID, user.getId());
        assertEquals("pum3xlr@gmail.com", user.getEmail());
        assertEquals("77011779469", user.getPersonalId());
        assertEquals("Andrzej", user.getName());
        assertEquals("Rączka", user.getSurname());
        assertEquals("946582113", user.getPhoneNumber());
        assertEquals(EXISTING_PATIENT_ADDRESS_ID, address.getId());
    }

    @Test
    void getUserById_shouldNotFind_throwNotFoundException() {
        assertThrows(NotFoundException.class, () -> userController.getUserById(9999L));
    }

    @Test
    void getUserByEmail_shouldNotFind_throwNotFoundException() {
        assertThrows(NotFoundException.class, () -> {
            Info info = new Info("zly-email@gmail.com", List.of(Role.DOCTOR));
            userController.getUser(info);
        });
    }

    @Transactional
    @Test
    void getDoctorById_shouldFind() {
        ResponseEntity<DoctorDto> doctorResponse = userController.getDoctorById(EXISTING_DOCTOR_ID);
        int statusCode = doctorResponse.getStatusCode().value();

        DoctorDto doctor = Objects.requireNonNull(doctorResponse.getBody());
        CompetencyInformationDto competencyInformation = doctor.getCompetencyInformation();

        assertEquals(HttpStatus.OK.value(), statusCode);
        assertEquals(EXISTING_DOCTOR_ID, doctor.getId());
        assertEquals("lukasz.raczka@gmail.com", doctor.getEmail());
        assertEquals("Łukasz", doctor.getName());
        assertEquals("Rączka", doctor.getSurname());
        assertEquals("554612576", doctor.getPhoneNumber());
        assertEquals(Sex.MALE, doctor.getSex());
        assertEquals(COMPETENCY_INFORMATION_ID, competencyInformation.getId());
        assertEquals(2, doctor.getServiceTypes().size());
    }

    @Test
    void getDoctorById_shouldNotFind_throwNotFoundException() {
        assertThrows(NotFoundException.class, () -> userController.getDoctorById(9999L));
    }

    @Transactional
    @Test
    void getDoctorByRequest_shouldFindTwo() {
        DoctorSearchRequestDto searchRequest = getDoctorSearchRequest_byName("Rączka");
        ResponseEntity<Page<DoctorDto>> doctorResponse = userController.getDoctorsByRequest(searchRequest);
        int statusCode = doctorResponse.getStatusCode().value();

        List<DoctorDto> doctors = Objects.requireNonNull(doctorResponse.getBody()).getContent();

        assertEquals(HttpStatus.OK.value(), statusCode);
        assertEquals(2, doctors.size());
    }


//    @Transactional
//    @Test
//    void createUser_shouldCreate() {
//        Info info = new Info("test@gmail.com", List.of(Role.PATIENT));
//        AddressDto address = getAddress(2L, "Rybnik", "44-260", "Jabłoniowa", "47");
//        UserPersonalDetailsDto personalDetails = getPersonalDetails("07211367537", "Jan", "Kowalski", "123456789", address);
//
//        ResponseEntity<UserPersonalDetailsDto> userResponse = userController.createUser(info, personalDetails);
//        int statusCode = userResponse.getStatusCode().value();
//
//        UserPersonalDetailsDto user = Objects.requireNonNull(userResponse.getBody());
//
//        assertEquals(HttpStatus.OK.value(), statusCode);
//        assertEquals("test@gmail.com", user.getEmail());
//        assertEquals(1, user.getRoles().size());
//        assertEquals("Jan", user.getName());
//        assertEquals("Kowalski", user.getSurname());
//        assertEquals("123456789", user.getPhoneNumber());
//        assertEquals("07211367537", user.getPersonalId());
//        assertNotNull(user.getAddress());
//    }

    @Transactional
    @Test
    void deleteUser_shouldDelete() {
        Info info = new Info("kasia@gmail.com", List.of(Role.DOCTOR));
        ResponseEntity<Void> deleteResponse = userController.deleteUser(info);
        int statusCode = deleteResponse.getStatusCode().value();

        assertEquals(HttpStatus.NO_CONTENT.value(), statusCode);
        assertThrows(NotFoundException.class, () -> userController.getUser(info));
    }

    AddressDto getAddress(Long voivodeshipId, String city, String postalCode, String street, String buildingNumber) {
        AddressDto address = new AddressDto();
        address.setVoivodeshipId(voivodeshipId);
        address.setCity(city);
        address.setPostalCode(postalCode);
        address.setStreet(street);
        address.setBuildingNumber(buildingNumber);
        return address;
    }

    UserPersonalDetailsDto getPersonalDetails(String pesel, String name, String surname, String phoneNumber, AddressDto address) {
        UserPersonalDetailsDto personalDetails = new UserPersonalDetailsDto();
        personalDetails.setPersonalId(pesel);
        personalDetails.setName(name);
        personalDetails.setSurname(surname);
        personalDetails.setPhoneNumber(phoneNumber);
        personalDetails.setAddress(address);
        return personalDetails;
    }

    VoivodeshipDto getVoivodeship(int id, String name) {
        VoivodeshipDto voivodeship = new VoivodeshipDto();
        voivodeship.setId(id);
        voivodeship.setName(name);
        return voivodeship;
    }

    DoctorSearchRequestDto getDoctorSearchRequest_byName(String name) {
        DoctorSearchRequestDto searchRequest = new DoctorSearchRequestDto();
        searchRequest.setName(name);
        return searchRequest;
    }
}

