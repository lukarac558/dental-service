package com.student.reservationservice.user.competency.service;

import com.student.api.CompetencyInformationDTO;
import com.student.reservationservice.common.exception.entity.NotFoundException;
import com.student.reservationservice.user.competency.entity.CompetencyInformation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.student.reservationservice.common.exception.entity.ErrorConstants.COMPETENCY_INFORMATION_NOT_FOUND_MESSAGE;

@RestController
@RequestMapping("/competency-information")
@Tag(name = "Competency information")
public class CompetencyResource {
    private final ModelMapper modelMapper;
    private final CompetencyService competencyService;

    @Autowired
    public CompetencyResource(CompetencyService competencyService, ModelMapper modelMapper) {
        this.competencyService = competencyService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{id}")
    @ApiResponse(responseCode = "404", description = "Competency information not found")
    @Operation(summary = "Find competency information by id.")
    public ResponseEntity<CompetencyInformationDTO> getCompetencyInformationById(@PathVariable("id") Long id) {
        CompetencyInformation competencyInformation = competencyService.findCompetencyInformationById(id)
                .orElseThrow(() -> new NotFoundException(String.format(COMPETENCY_INFORMATION_NOT_FOUND_MESSAGE, id)));
        return new ResponseEntity<>(modelMapper.map(competencyInformation, CompetencyInformationDTO.class), HttpStatus.OK);
    }

    @PostMapping("/")
    @Operation(summary = "Add new competency information.")
    public ResponseEntity<CompetencyInformationDTO> addCompetencyInformation(@RequestParam String title,
                                                                             @RequestParam String description) {
        CompetencyInformation addedCompetencyInformation
                = competencyService.addOrUpdateCompetencyInformation(new CompetencyInformation(title, description));
        return new ResponseEntity<>(modelMapper.map(addedCompetencyInformation, CompetencyInformationDTO.class), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiResponse(responseCode = "404", description = "Competency information not found")
    @Operation(summary = "Update competency information by id.")
    public ResponseEntity<CompetencyInformationDTO> updateCompetencyInformation(@PathVariable("id") Long id,
                                                                                @RequestParam String title,
                                                                                @RequestParam String description) {
        CompetencyInformation competencyInformation = competencyService.findCompetencyInformationById(id)
                .map(c -> {
                    c.setTitle(title);
                    c.setDescription(description);
                    return c;
                })
                .orElseThrow(() -> new NotFoundException(String.format(COMPETENCY_INFORMATION_NOT_FOUND_MESSAGE, id)));

        CompetencyInformation updatedCompetencyInformation = competencyService.addOrUpdateCompetencyInformation(competencyInformation);
        return new ResponseEntity<>(modelMapper.map(updatedCompetencyInformation, CompetencyInformationDTO.class), HttpStatus.OK);
    }
}
