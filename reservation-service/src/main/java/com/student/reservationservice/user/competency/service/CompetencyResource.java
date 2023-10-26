package com.student.reservationservice.user.competency.service;

import com.student.api.CompetencyInformationCreationDTO;
import com.student.api.CompetencyInformationDTO;
import com.student.reservationservice.user.competency.entity.CompetencyInformation;
import com.student.reservationservice.user.competency.exception.CompetencyNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/competency-information")
public class CompetencyResource {
    private final ModelMapper modelMapper;
    private final CompetencyService competencyService;

    @Autowired
    public CompetencyResource(CompetencyService competencyService, ModelMapper modelMapper) {
        this.competencyService = competencyService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<CompetencyInformationDTO> getCompetencyInformationById(@PathVariable("id") Long id) {
        CompetencyInformation competencyInformation = competencyService.findCompetencyInformationById(id)
                .orElseThrow(() -> new CompetencyNotFoundException(id));
        return new ResponseEntity<>(modelMapper.map(competencyInformation, CompetencyInformationDTO.class), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<CompetencyInformationDTO> addCompetencyInformation(@RequestBody CompetencyInformationCreationDTO competencyInformationCreationDTO) {
        CompetencyInformation addedCompetencyInformation
                = competencyService.addOrUpdateCompetencyInformation(modelMapper.map(competencyInformationCreationDTO, CompetencyInformation.class));
        return new ResponseEntity<>(modelMapper.map(addedCompetencyInformation, CompetencyInformationDTO.class), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CompetencyInformationDTO> updateCompetencyInformation(@PathVariable("id") Long id,
                                                                                @RequestParam String title,
                                                                                @RequestParam String description) {
        CompetencyInformation competencyInformation = competencyService.findCompetencyInformationById(id)
                .map(c -> {
                    c.setTitle(title);
                    c.setDescription(description);
                    return c;
                })
                .orElseThrow(() -> new CompetencyNotFoundException(id));

        CompetencyInformation updatedCompetencyInformation = competencyService.addOrUpdateCompetencyInformation(competencyInformation);
        return new ResponseEntity<>(modelMapper.map(updatedCompetencyInformation, CompetencyInformationDTO.class), HttpStatus.OK);
    }
}
