package com.student.userservice.controller;

import com.student.api.annotation.extractor.auth.Info;
import com.student.api.dto.common.enums.Role;
import com.student.api.dto.user.CompetencyInformationDto;
import com.student.api.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CompetencyControllerTest extends BaseControllerTests {
    @Autowired
    CompetencyController competencyController;
    static final Long EXISTING_COMPETENCY_INFORMATION_ID = 2222L;
    static final Long EXISTING__ANOTHER_COMPETENCY_INFORMATION_ID = 2233L;

    @Test
    void getCompetencyInformation_shouldFind() {
        Info info = new Info("lukasz.raczka@gmail.com", List.of(Role.DOCTOR));
        ResponseEntity<CompetencyInformationDto> competencyResponse = competencyController.getCompetencyInformation(info);

        int statusCode = competencyResponse.getStatusCode().value();
        CompetencyInformationDto competencyInformation = Objects.requireNonNull(competencyResponse.getBody());

        assertEquals(HttpStatus.OK.value(), statusCode);
        assertEquals(EXISTING_COMPETENCY_INFORMATION_ID, competencyInformation.getId());
        assertEquals("lek. dent.", competencyInformation.getTitle());
        assertEquals("Doświadczony lekarz.", competencyInformation.getDescription());
    }

    @Test
    void getCompetencyInformation_shouldNotFind_throwNotFoundException() {
        assertThrows(NotFoundException.class, () -> {
            Info info = new Info("zly-email@gmail.com", List.of(Role.DOCTOR));
            competencyController.getCompetencyInformation(info);
        });
    }

    @Test
    void createCompetencyInformation_shouldCreate() {
        String exampleTitle = "lek. dent. dr hab. n. med.";
        String exampleDescription = "Opis kompetencji";
        CompetencyInformationDto competencyInformationDto = getCompetencyInformationDto(exampleTitle, exampleDescription);
        Info info = new Info("kasia@gmail.com", List.of(Role.DOCTOR));
        ResponseEntity<CompetencyInformationDto> competencyResponse = competencyController.createCompetencyInformation(info, competencyInformationDto);

        int statusCode = competencyResponse.getStatusCode().value();
        CompetencyInformationDto competencyInformation = Objects.requireNonNull(competencyResponse.getBody());

        assertEquals(HttpStatus.CREATED.value(), statusCode);
        assertEquals(exampleTitle, competencyInformation.getTitle());
        assertEquals(exampleDescription, competencyInformation.getDescription());
    }

    @Test
    void updateCompetencyInformation_shouldUpdate() {
        String exampleTitle = "lek. dent. dr hab. n. med.";
        String exampleDescription = "Dentysta z ogromnym doświadczeniem.";
        CompetencyInformationDto competencyInformationDto = getCompetencyInformationDto(exampleTitle, exampleDescription);
        Info info = new Info("maja.osa@gmail.com", List.of(Role.DOCTOR));
        ResponseEntity<CompetencyInformationDto> competencyResponse = competencyController.updateCompetencyInformation(info, competencyInformationDto);

        int statusCode = competencyResponse.getStatusCode().value();
        CompetencyInformationDto competencyInformation = Objects.requireNonNull(competencyResponse.getBody());

        assertEquals(HttpStatus.OK.value(), statusCode);
        assertEquals(exampleTitle, competencyInformation.getTitle());
        assertEquals(exampleDescription, competencyInformation.getDescription());
    }

    CompetencyInformationDto getCompetencyInformationDto(String title, String description) {
        CompetencyInformationDto competencyInformation = new CompetencyInformationDto();
        competencyInformation.setTitle(title);
        competencyInformation.setDescription(description);
        return competencyInformation;
    }

}

