package com.student.userservice.controller;

import com.student.api.annotation.extractor.auth.AuthInfo;
import com.student.api.annotation.extractor.auth.Info;
import com.student.api.dto.user.CompetencyInformationDto;
import com.student.userservice.entity.CompetencyInformationEntity;
import com.student.userservice.service.competency.CompetencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/competency-information")
@Tag(name = "Competency information")
@RequiredArgsConstructor
public class CompetencyController {
    private final ModelMapper modelMapper;
    private final CompetencyService competencyService;

    @GetMapping("")
    @ApiResponse(responseCode = "404", description = "Competency information not found")
    @Operation(summary = "Find competency information of current user")
    public ResponseEntity<CompetencyInformationDto> getCompetencyInformation(
            @Parameter(hidden = true)
            @AuthInfo Info info
    ) {
        CompetencyInformationEntity competencyInformation = competencyService.findCompetencyInformation(info);
        return new ResponseEntity<>(modelMapper.map(competencyInformation, CompetencyInformationDto.class), HttpStatus.OK);
    }

    @PostMapping("")
    @Operation(summary = "Add new competency information.")
    public ResponseEntity<CompetencyInformationDto> addCompetencyInformation(
            @Parameter(hidden = true)
            @AuthInfo Info info,
            @RequestBody CompetencyInformationDto competencyInformation
    ) {
        CompetencyInformationEntity competencyInformationEntity = modelMapper.map(
                competencyInformation,
                CompetencyInformationEntity.class
        );


        return new ResponseEntity<>(
                modelMapper.map(
                        competencyService.createCompetencyInformation(
                                info,
                                competencyInformationEntity
                        ),
                        CompetencyInformationDto.class
                ),
                HttpStatus.CREATED
        );
    }

    @PutMapping("")
    @ApiResponse(responseCode = "404", description = "Competency information not found")
    @Operation(summary = "Update competency information by id.")
    public ResponseEntity<CompetencyInformationDto> updateCompetencyInformation(
            @Parameter(hidden = true)
            @AuthInfo Info info,
            @RequestBody CompetencyInformationDto competencyInformation
    ) {
        CompetencyInformationEntity competencyInformationEntity = modelMapper.map(
                competencyInformation,
                CompetencyInformationEntity.class
        );

        return new ResponseEntity<>(
                modelMapper.map(
                        competencyService.updateCompetencyInformation(
                                info,
                                competencyInformationEntity
                        ),
                        CompetencyInformationDto.class
                ),
                HttpStatus.CREATED
        );
    }
}
