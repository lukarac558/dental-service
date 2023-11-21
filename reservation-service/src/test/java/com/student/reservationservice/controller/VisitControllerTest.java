package com.student.reservationservice.controller;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.student.api.dto.common.enums.Role;
import com.student.api.dto.common.enums.Sex;
import com.student.api.dto.reservation.*;
import com.student.api.dto.user.AddressDto;
import com.student.api.dto.user.DoctorDto;
import com.student.api.dto.user.ServiceTypeDto;
import com.student.api.dto.user.UserPersonalDetailsDto;
import com.student.api.exception.IncorrectValueException;
import com.student.api.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.junit.jupiter.api.Assertions.*;

class VisitControllerTest extends BaseControllerTests {
    @Autowired
    VisitController visitController;
    static final Long EXISTING_VISIT_ID = 1111L;
    static final Long EXISTING_DOCTOR_ID = 4444L;
    static final Long EXISTING_PATIENT_ID = 4455L;
    static final Long EXISTING_SERVICE_TYPE_ID = 6666L;
    static final Long EXISTING_ANOTHER_SERVICE_TYPE_ID = 6677L;
    static final List<ServiceTypeDto> SERVICE_TYPES = List.of(
            getServiceType(EXISTING_SERVICE_TYPE_ID, "Plombowanie", "01:00:00", "Opis uslugi plombowania zeba."),
            getServiceType(EXISTING_ANOTHER_SERVICE_TYPE_ID, "Wyrywanie", "00:30:00", "Opis uslugi wyrywania zeba.")
    );

    @SneakyThrows
    @Test
    @Transactional
    void getVisitById_shouldFind() {
        // given
        stubForServiceTypesByIds();
        stubForDoctorById();

        // when
        ResponseEntity<VisitReservationDetailDto> visitResponse = visitController.getVisitById(EXISTING_VISIT_ID);

        // then
        int statusCode = visitResponse.getStatusCode().value();
        VisitReservationDetailDto visit = Objects.requireNonNull(visitResponse.getBody());
        VisitDetailDto visitDetail = visit.getVisit();
        List<VisitPositionDetailDto> visitPositionsDetail = visit.getVisitPositionDetails();

        assertEquals(HttpStatus.OK.value(), statusCode);
        assertEquals(EXISTING_VISIT_ID, visitDetail.getId());
        assertEquals(EXISTING_PATIENT_ID, visitDetail.getPatientId());
        assertEquals(2, visitPositionsDetail.size());
    }

    @Test
    void getVisitById_shouldNotFind_throwNotFoundException() {
        assertThrows(NotFoundException.class, () -> visitController.getVisitById(9999L));
    }

    @SneakyThrows
    @Test
    @Transactional
    void getVisitStartDatesByServiceTypeIds_shouldFind() {
        // given
        stubForServiceTypesByIds();
        stubForServiceTypesByDoctorId();
        stubForDoctorById();

        // when
        List<Long> serviceTypeIds = SERVICE_TYPES.stream().map(ServiceTypeDto::getId).toList();
        ResponseEntity<List<String>> startDatesResponse = visitController.getVisitStartDatesByServiceTypeIds(serviceTypeIds);

        // then
        int statusCode = startDatesResponse.getStatusCode().value();
        List<String> startDates = Objects.requireNonNull(startDatesResponse.getBody());

        assertEquals(HttpStatus.OK.value(), statusCode);
        assertFalse(startDates.isEmpty());
        assertTrue(startDates.contains("2024-03-18 12:00:00"));
    }

    @SneakyThrows
    @Test
    @Transactional
    void approveVisit_shouldApprove() {
        // given
        stubForServiceTypesByIds();
        stubForServiceTypesByDoctorId();
        stubForDoctorById();
        stubForPatientById();

        VisitReservationDto visitReservation = getVisitReservation("2024-03-18 12:00:00");
        ResponseEntity<VisitReservationDetailDto> visitResponse = visitController.reserveVisit(visitReservation);
        VisitReservationDetailDto visitReservationDetail = Objects.requireNonNull(visitResponse.getBody());
        VisitDetailDto visitDetail = visitReservationDetail.getVisit();

        // when
        ResponseEntity<VisitDto> approvedVisitResponse = visitController.approveVisit(visitDetail.getId());

        // then
        int statusCode = approvedVisitResponse.getStatusCode().value();
        assertEquals(HttpStatus.OK.value(), statusCode);
    }


    @SneakyThrows
    @Test
    @Transactional
    void reserveVisit_shouldReserve() {
        // given
        stubForServiceTypesByIds();
        stubForServiceTypesByDoctorId();
        stubForDoctorById();
        stubForPatientById();

        VisitReservationDto visitReservation = getVisitReservation("2024-03-18 10:00:00");

        // when
        ResponseEntity<VisitReservationDetailDto> visitResponse = visitController.reserveVisit(visitReservation);

        // then
        int statusCode = visitResponse.getStatusCode().value();
        VisitReservationDetailDto visitReservationDetail = Objects.requireNonNull(visitResponse.getBody());
        VisitDetailDto visitDetail = visitReservationDetail.getVisit();
        List<VisitPositionDetailDto> visitPositionDetails = visitReservationDetail.getVisitPositionDetails();

        assertEquals(HttpStatus.CREATED.value(), statusCode);
        assertNotNull(visitDetail.getId());
        assertEquals(2, visitPositionDetails.size());
    }

    @SneakyThrows
    @Test
    @Transactional
    void reserveVisit_tryReserveTwoVisitsParallel_oneShouldThrowException() {
        // given
        stubForPatientById();
        stubForServiceTypesByIds();
        stubForServiceTypesByDoctorId();
        stubForDoctorById();

        VisitReservationDto visitReservation = getVisitReservation("2024-03-19 09:00:00");

        CompletableFuture<ResponseEntity<VisitReservationDetailDto>> future1 = CompletableFuture.supplyAsync(() ->
        {
            try {
                return visitController.reserveVisit(visitReservation);
            } catch (IncorrectValueException ex) {
                return null;
            }
        });

        CompletableFuture<ResponseEntity<VisitReservationDetailDto>> future2 = CompletableFuture.supplyAsync(() ->
        {
            try {
                return visitController.reserveVisit(visitReservation);
            } catch (IncorrectValueException ex) {
                return null;
            }
        });

        CompletableFuture.allOf(future1, future2).join();

        Optional<ResponseEntity<VisitReservationDetailDto>> response1 = Optional.ofNullable(future1.get());
        Optional<ResponseEntity<VisitReservationDetailDto>> response2 = Optional.ofNullable(future2.get());

        assertTrue(response1.isEmpty() || response2.isEmpty());
        assertExpectedStartDate("2024-03-19 09:00:00.0", response1, response2);
    }

    @SneakyThrows
    @Test
    @Transactional
    void reserveVisit_shouldNotReserve_GivenStartDateIsAlreadyReserved() {
        stubForServiceTypesByIds();
        stubForServiceTypesByDoctorId();
        stubForDoctorById();
        stubForPatientById();

        VisitReservationDto visitReservation = getVisitReservation("2024-03-18 08:00:00");

        assertThrows(IncorrectValueException.class, () -> visitController.reserveVisit(visitReservation));
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

    @Transactional
    @Test
    void deleteNotApprovedVisitsWithExpiredTime_shouldDeleteNothing() {
        ResponseEntity<?> deleteResponse = visitController.deleteNotApprovedVisitsWithExpiredTime();

        int statusCode = deleteResponse.getStatusCode().value();
        assertEquals(HttpStatus.NO_CONTENT.value(), statusCode);
    }

    private void stubForServiceTypesByIds() throws JsonProcessingException {
        String queryParams = "ids=" + EXISTING_SERVICE_TYPE_ID + "&ids=" + EXISTING_ANOTHER_SERVICE_TYPE_ID;
        server.stubFor(WireMock
                .get(String.format("/service-types?%s", queryParams))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(new ObjectMapper().writeValueAsString(SERVICE_TYPES))
                        .withStatus(HttpStatus.OK.value())
                )
        );
    }

    private void stubForServiceTypesByDoctorId() throws JsonProcessingException {
        server.stubFor(WireMock
                .get(String.format("/service-types/doctor/%s", EXISTING_DOCTOR_ID))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(new ObjectMapper().writeValueAsString(SERVICE_TYPES))
                        .withStatus(HttpStatus.OK.value())
                )
        );
    }

    private void stubForDoctorById() throws JsonProcessingException {
        DoctorDto doctor = getDoctor(new HashSet<>(SERVICE_TYPES));
        server.stubFor(WireMock
                .get(String.format("/users/doctor/%s", EXISTING_DOCTOR_ID))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(new ObjectMapper().writeValueAsString(doctor))
                        .withStatus(HttpStatus.OK.value())
                )
        );
    }

    private void stubForPatientById() throws JsonProcessingException {
        AddressDto address = getAddress();
        UserPersonalDetailsDto personalDetails = getPersonalDetails(address
        );
        server.stubFor(WireMock
                .get(String.format("/users/%s", EXISTING_PATIENT_ID))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(new ObjectMapper().writeValueAsString(personalDetails))
                        .withStatus(HttpStatus.OK.value())
                )
        );
    }

    private static ServiceTypeDto getServiceType(Long id, String name, String duration, String description) {
        ServiceTypeDto serviceType = new ServiceTypeDto();
        serviceType.setId(id);
        serviceType.setName(name);
        serviceType.setDurationTime(duration);
        serviceType.setDescription(description);
        serviceType.setDoctorId(VisitControllerTest.EXISTING_DOCTOR_ID);
        serviceType.setEnabled(true);
        return serviceType;

    }

    private DoctorDto getDoctor(Set<ServiceTypeDto> serviceTypes) {
        DoctorDto doctor = new DoctorDto();
        doctor.setId(4444L);
        doctor.setEmail("lukasz.raczka@gmail.com");
        doctor.setName("Łukasz");
        doctor.setSurname("Rączka");
        doctor.setSex(Sex.MALE);
        doctor.setAge(26);
        doctor.setPhoneNumber("554612576");
        doctor.setServiceTypes(serviceTypes);
        return doctor;
    }

    private VisitReservationDto getVisitReservation(String startDate) {
        VisitReservationDto visitReservation = new VisitReservationDto();
        VisitDto visit = new VisitDto();
        visit.setStartDate(startDate);
        visit.setPatientId(VisitControllerTest.EXISTING_PATIENT_ID);

        visitReservation.setVisit(visit);
        visitReservation.setServiceTypeIds(VisitControllerTest.SERVICE_TYPES.stream().map(ServiceTypeDto::getId).toList());
        return visitReservation;
    }

    private UserPersonalDetailsDto getPersonalDetails(AddressDto address) {
        UserPersonalDetailsDto personalDetails = new UserPersonalDetailsDto();
        personalDetails.setId(EXISTING_PATIENT_ID);
        personalDetails.setPersonalId("77011779469");
        personalDetails.setName("Andrzej");
        personalDetails.setSurname("Rączka");
        personalDetails.setPhoneNumber("946582113");
        personalDetails.setAddress(address);
        personalDetails.setEmail("pum3xlr@gmail.com");
        personalDetails.setSex(Sex.MALE);
        personalDetails.setBirthDate("1977-01-17");
        personalDetails.setRoles(Set.of(Role.PATIENT.getRoleName()));
        return personalDetails;
    }

    private AddressDto getAddress() {
        AddressDto address = new AddressDto();
        address.setVoivodeshipId(2L);
        address.setCity("Wodzisław Śląski");
        address.setPostalCode("44-300");
        address.setStreet("Pomidorowa");
        address.setBuildingNumber("16a");
        return address;
    }

    private void assertExpectedStartDate(String expectedStartDate, Optional<ResponseEntity<VisitReservationDetailDto>> response1, Optional<ResponseEntity<VisitReservationDetailDto>> response2) {
        if (response1.isPresent()) {
            assertEquals(expectedStartDate, response1.get().getBody().getVisit().getStartDate());
        } else {
            assertEquals(expectedStartDate, response2.get().getBody().getVisit().getStartDate());
        }
    }
}

