package com.student.reservationservice.controller;

import com.student.api.dto.reservation.VisitDto;
import com.student.api.exception.NotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VisitControllerTest extends BaseControllerTests {
    @Autowired
    VisitController visitController;
    static final Long EXISTING_VISIT_ID = 1111L;

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

