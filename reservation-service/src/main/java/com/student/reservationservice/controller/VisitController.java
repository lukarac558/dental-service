package com.student.reservationservice.controller;

import com.student.api.dto.reservation.*;
import com.student.api.dto.user.ServiceTypeDto;
import com.student.api.dto.user.UserPersonalDetailsDto;
import com.student.api.exception.NotFoundException;
import com.student.api.util.TimestampFormatParser;
import com.student.reservationservice.entity.VisitEntity;
import com.student.reservationservice.entity.VisitPositionEntity;
import com.student.reservationservice.service.VisitPositionService;
import com.student.reservationservice.service.visit.VisitService;
import com.student.reservationservice.user.UserClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

import static com.student.api.exception.handler.ErrorConstants.SERVICE_TYPE_NOT_FOUND_MESSAGE;
import static com.student.api.exception.handler.ErrorConstants.VISIT_NOT_FOUND_MESSAGE;

@RestController
@RequestMapping("/visits")
@Tag(name = "Visits")
@RequiredArgsConstructor
public class VisitController {
    private final ModelMapper modelMapper;
    private final VisitService visitService;
    private final VisitPositionService visitPositionService;
    private final UserClient userClient;

    @GetMapping("/{id}")
    @ApiResponse(responseCode = "404", description = "Visit not found")
    @Operation(summary = "Find visit by id")
    public ResponseEntity<VisitDto> getVisitById(@PathVariable("id") Long id) {
        VisitEntity visitEntity = visitService.findVisitById(id)
                .orElseThrow(() -> new NotFoundException(String.format(VISIT_NOT_FOUND_MESSAGE, id)));
        return new ResponseEntity<>(modelMapper.map(visitEntity, VisitDto.class), HttpStatus.OK);
    }

    @GetMapping("/not-approved/{user_id}")
    @Operation(summary = "Find all patient not approved visits by his id.")
    public ResponseEntity<List<VisitDto>> getNotApprovedVisitsByUserId(@PathVariable("user_id") Long userId) {
        List<VisitEntity> visitEntities = visitService.findNotApprovedVisitsByUserId(userId);
        List<VisitDto> visitsResponse = visitEntities.stream().map(v -> modelMapper.map(v, VisitDto.class)).toList();
        return new ResponseEntity<>(visitsResponse, HttpStatus.OK);
    }

    @PostMapping("/historical")
    @Operation(summary = "Find all patient historical visits by search parameters.")
    public ResponseEntity<Page<VisitDto>> getHistoricalVisitsByUserId(
            @Validated @RequestBody VisitSearchRequestDto searchRequestDto) {
        Page<VisitEntity> visitEntities = visitService.findHistoricalVisitsByUserId(searchRequestDto);
        return new ResponseEntity<>(visitEntities.map(v -> modelMapper.map(v, VisitDto.class)), HttpStatus.OK);
    }

    @PostMapping("/approved")
    @Operation(summary = "Find all patient approved upcoming visits by search parameters.")
    public ResponseEntity<Page<VisitDto>> getApprovedVisitsByUserId(
            @Validated @RequestBody VisitSearchRequestDto searchRequestDto
    ) {
        Page<VisitEntity> visitEntities = visitService.findApprovedUpComingVisitsByUserId(searchRequestDto);
        return new ResponseEntity<>(visitEntities.map(v -> modelMapper.map(v, VisitDto.class)), HttpStatus.OK);
    }

    @PostMapping("")
    @ApiResponse(responseCode = "404", description = "User or service type not found")
    @ApiResponse(responseCode = "422", description = "Incorrect timestamp format is given")
    @Operation(summary = "Reserve visit for the patient.")
    public ResponseEntity<VisitReservationDetailDto> reserveVisit(@RequestBody VisitReservationDto visitReservationDto) {
        VisitDto visit = visitReservationDto.getVisitDto();
        List<Long> serviceTypeIds = visitReservationDto.getServiceTypeIds();

        UserPersonalDetailsDto user = userClient.getUserById(visit.getPatientId());
        List<ServiceTypeDto> serviceTypes = userClient.getServiceTypes(serviceTypeIds);

        visitService.validateStartDateOrThrow(serviceTypeIds, visit.getStartDate());

        VisitEntity visitEntity = visitService.addOrUpdateVisit(mapToVisit(visit, user.getId()));
        List<VisitPositionEntity> visitPositionEntities = visitPositionService.addVisitPositions(visitEntity, serviceTypeIds);

        VisitDto visitDTO = mapToVisitDTO(visitEntity, user.getId());
        List<VisitPositionDetailDto> visitPositionDetails = mapToVisitPositionDetails(visitPositionEntities, serviceTypes);

        return new ResponseEntity<>(new VisitReservationDetailDto(visitDTO, visitPositionDetails), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiResponse(responseCode = "404", description = "Visit not found")
    @Operation(summary = "Update visit description by id.")
    public ResponseEntity<VisitDto> updateVisitDescription(@PathVariable("id") Long id,
                                                           @RequestParam String description) {
        VisitEntity visitEntity = visitService.findVisitById(id)
                .map(v -> {
                    v.setDescription(description);
                    return v;
                })
                .orElseThrow(() -> new NotFoundException(String.format(VISIT_NOT_FOUND_MESSAGE, id)));

        VisitEntity updatedVisitEntity = visitService.addOrUpdateVisit(visitEntity);
        return new ResponseEntity<>(modelMapper.map(updatedVisitEntity, VisitDto.class), HttpStatus.OK);
    }

    @PutMapping("approve/{id}")
    @ApiResponse(responseCode = "404", description = "Visit approval is forbidden")
    @ApiResponse(responseCode = "404", description = "Visit not found")
    @Operation(summary = "Approve visit by id.")
    public ResponseEntity<VisitDto> approveVisit(@PathVariable("id") Long id) {
        VisitEntity visitEntity = visitService.findVisitById(id)
                .orElseThrow(() -> new NotFoundException(String.format(VISIT_NOT_FOUND_MESSAGE, id)));

        visitService.setVisitAsApprovedOrThrow(visitEntity);

        VisitEntity updatedVisitEntity = visitService.addOrUpdateVisit(visitEntity);
        return new ResponseEntity<>(modelMapper.map(updatedVisitEntity, VisitDto.class), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "403", description = "Visit cancellation is forbidden")
    @ApiResponse(responseCode = "404", description = "Visit not found")
    @Operation(summary = "Cancel visit by id.")
    public ResponseEntity<Void> deleteVisit(@PathVariable("id") Long id) {
        visitService.deleteVisit(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/not-approved")
    @Operation(summary = "Cancel not approved visits with expired time.")
    public ResponseEntity<Void> deleteNotApprovedVisitsWithExpiredTime() {
        visitService.deleteNotApprovedVisitsWithExpiredTime();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("available-times/{service_type_ids}")
    @Operation(summary = "Find all available visit start dates by service type ids.")
    public ResponseEntity<List<String>> getVisitStartDatesByServiceTypeIds(@PathVariable("service_type_ids") List<Long> serviceTypeIds) {
        List<String> visitStartDates = visitService.findAvailableVisitTimesInIntervals(serviceTypeIds);
        return new ResponseEntity<>(visitStartDates, HttpStatus.OK);
    }

    private VisitEntity mapToVisit(VisitDto visitDto, Long patientId) {
        VisitEntity visitEntity = new VisitEntity();
        visitEntity.setStartDate(TimestampFormatParser.parseOrThrow(visitDto.getStartDate()));
        visitEntity.setReservationDate(new Timestamp(System.currentTimeMillis()));
        visitEntity.setPatientId(patientId);
        visitEntity.setApproved(false);
        return visitEntity;
    }

    private VisitDto mapToVisitDTO(VisitEntity visitEntity, Long patientId) {
        VisitDto visitDTO = modelMapper.map(visitEntity, VisitDto.class);
        visitDTO.setPatientId(patientId);
        return visitDTO;
    }

    private List<VisitPositionDetailDto> mapToVisitPositionDetails(List<VisitPositionEntity> visitPositionEntities, List<ServiceTypeDto> serviceTypes) {
        return visitPositionEntities.stream()
                .map(vp -> {
                    ServiceTypeDto serviceType = getServiceTypeOrThrow(serviceTypes, vp.getServiceTypeId());
                    return new VisitPositionDetailDto(vp.getId(), serviceType);
                })
                .toList();
    }

    private ServiceTypeDto getServiceTypeOrThrow(List<ServiceTypeDto> serviceTypes, Long serviceTypeId) {
        return serviceTypes.stream().filter(type -> type.getId().equals(serviceTypeId)).findFirst()
                .orElseThrow(() -> new NotFoundException(String.format(SERVICE_TYPE_NOT_FOUND_MESSAGE, serviceTypeId)));

    }
}
