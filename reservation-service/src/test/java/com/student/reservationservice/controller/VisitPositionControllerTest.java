package com.student.reservationservice.controller;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.student.api.dto.reservation.VisitPositionDto;
import com.student.api.dto.user.ServiceTypeDto;
import com.student.api.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VisitPositionControllerTest extends BaseControllerTests {

    @Autowired
    VisitPositionController visitPositionController;
    static final Long EXISTING_DOCTOR_ID = 4444L;
    static final Long EXISTING_VISIT_POSITION_ID = 9999L;
    static final Long EXISTING_VISIT_ID = 1111L;
    static final Long EXISTING_SERVICE_TYPE_ID = 6666L;

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

    @SneakyThrows
    @Transactional
    @Test
    void createVisitPosition_shouldCreate() {
        ServiceTypeDto serviceType = new ServiceTypeDto();
        serviceType.setId(1234L);
        List<ServiceTypeDto> serviceTypes = List.of(serviceType);
        server.stubFor(WireMock
                .get(String.format("/service-types/doctor/%s", EXISTING_DOCTOR_ID))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(new org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(serviceTypes))
                        .withStatus(HttpStatus.OK.value())
                )
        );

        ResponseEntity<VisitPositionDto> visitPositionResponse = visitPositionController.createVisitPosition(EXISTING_VISIT_ID, EXISTING_SERVICE_TYPE_ID);

        int statusCode = visitPositionResponse.getStatusCode().value();
        VisitPositionDto visitPosition = Objects.requireNonNull(visitPositionResponse.getBody());

        assertEquals(HttpStatus.CREATED.value(), statusCode);
        assertNotNull(visitPosition.getId());
        assertEquals(EXISTING_VISIT_ID, visitPosition.getVisitId());
        assertEquals(EXISTING_SERVICE_TYPE_ID, visitPosition.getServiceTypeId());
    }

    @Transactional
    @Test
    void deleteVisitPosition_shouldDelete() {
        ResponseEntity<?> deleteResponse = visitPositionController.deleteVisitPosition(EXISTING_VISIT_POSITION_ID);

        int statusCode = deleteResponse.getStatusCode().value();

        assertEquals(HttpStatus.NO_CONTENT.value(), statusCode);
        assertThrows(NotFoundException.class, () -> visitPositionController.getVisitPositionById(EXISTING_VISIT_POSITION_ID));
    }
}

