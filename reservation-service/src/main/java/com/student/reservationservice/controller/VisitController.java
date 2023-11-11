package com.student.reservationservice.controller;

import com.student.api.dto.reservation.*;
import com.student.api.dto.user.DoctorDto;
import com.student.api.dto.user.ServiceTypeDetailDto;
import com.student.api.dto.user.ServiceTypeDto;
import com.student.api.dto.user.UserPersonalDetailsDto;
import com.student.api.exception.NotFoundException;
import com.student.api.util.TimeHelper;
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

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

import static com.student.api.exception.handler.ErrorConstants.*;

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
    @ApiResponse(responseCode = "404", description = "Visit or doctor not found")
    @Operation(summary = "Find visit by id")
    public ResponseEntity<VisitReservationDetailDto> getVisitById(@PathVariable("id") Long id) {
        VisitEntity visitEntity = visitService.findVisitById(id)
                .orElseThrow(() -> new NotFoundException(String.format(VISIT_NOT_FOUND_MESSAGE, id)));
        return new ResponseEntity<>(mapToVisitReservationDetail(visitEntity), HttpStatus.OK);
    }

    @GetMapping("available-times/{service_type_ids}")
    @Operation(summary = "Find all available visit start dates by service type ids.")
    public ResponseEntity<List<String>> getVisitStartDatesByServiceTypeIds(@PathVariable("service_type_ids") List<Long> serviceTypeIds) {
        List<String> visitStartDates = visitService.findAvailableVisitTimesInIntervals(serviceTypeIds);
        return new ResponseEntity<>(visitStartDates, HttpStatus.OK);
    }

    @PostMapping("/not-approved")
    @Operation(summary = "Find all patient not approved visits by provided search object.")
    public ResponseEntity<Page<VisitReservationDetailDto>> getNotApprovedVisitsByPatientId(
            @Validated @RequestBody VisitSearchRequestDto searchRequestDto) {
        Page<VisitEntity> visitEntities = visitService.findNotApprovedVisitsByRequest(searchRequestDto);
        return new ResponseEntity<>(mapToVisitReservationDetails(visitEntities), HttpStatus.OK);
    }

    @PostMapping("/approved")
    @Operation(summary = "Find all patient approved visits by provided search object.")
    public ResponseEntity<Page<VisitReservationDetailDto>> getApprovedVisitsByRequest(
            @Validated @RequestBody VisitSearchRequestDto searchRequestDto) {
        Page<VisitEntity> visitEntities = visitService.findApprovedVisitsByRequest(searchRequestDto);
        return new ResponseEntity<>(mapToVisitReservationDetails(visitEntities), HttpStatus.OK);
    }

    @PostMapping("")
    @ApiResponse(responseCode = "404", description = "User or service type not found")
    @ApiResponse(responseCode = "422", description = "Incorrect timestamp format is given")
    @Operation(summary = "Reserve visit for the patient.")
    public ResponseEntity<VisitReservationDetailDto> reserveVisit(@RequestBody VisitReservationDto visitReservationDto) {
        VisitDto visit = visitReservationDto.getVisit();
        List<Long> serviceTypeIds = visitReservationDto.getServiceTypeIds();

        visitService.validateStartDateOrThrow(serviceTypeIds, visit.getStartDate());

        UserPersonalDetailsDto user = userClient.getUserById(visit.getPatientId());
        VisitEntity visitEntity = visitService.addOrUpdateVisit(mapToVisitEntity(visit, user.getId()));
        List<VisitPositionEntity> visitPositionsEntities = visitPositionService.addVisitPositions(visitEntity, serviceTypeIds);

        return new ResponseEntity<>(mapToVisitReservationDetail(visitEntity, visitPositionsEntities), HttpStatus.CREATED);
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

    private VisitEntity mapToVisitEntity(VisitDto visitDto, Long patientId) {
        VisitEntity visitEntity = new VisitEntity();
        visitEntity.setStartDate(TimestampFormatParser.parseOrThrow(visitDto.getStartDate()));
        visitEntity.setReservationDate(new Timestamp(System.currentTimeMillis()));
        visitEntity.setPatientId(patientId);
        visitEntity.setApproved(false);
        return visitEntity;
    }

    private Page<VisitReservationDetailDto> mapToVisitReservationDetails(Page<VisitEntity> visitEntities) {
        return visitEntities.map(this::mapToVisitReservationDetail);
    }

    private List<VisitReservationDetailDto> mapToVisitReservationDetails(List<VisitEntity> visitEntities) {
        return visitEntities.stream().map(this::mapToVisitReservationDetail).toList();
    }

    private VisitReservationDetailDto mapToVisitReservationDetail(VisitEntity visitEntity) {
        return mapToVisitReservationDetail(visitEntity, visitEntity.getVisitPositions());
    }

    private VisitReservationDetailDto mapToVisitReservationDetail(VisitEntity visitEntity, List<VisitPositionEntity> visitPositionEntities) {
        VisitDetailDto visitDetail = mapToVisitDetail(visitEntity, visitPositionEntities);
        List<VisitPositionDetailDto> visitPositionDetails = mapToVisitPositionDetails(visitPositionEntities);
        return new VisitReservationDetailDto(visitDetail, visitPositionDetails);
    }

    private List<ServiceTypeDto> getServiceTypes(List<VisitPositionEntity> visitPositionEntities) {
        List<Long> serviceTypeIds = visitPositionEntities.stream().map(VisitPositionEntity::getServiceTypeId).toList();
        return userClient.getServiceTypes(serviceTypeIds);
    }

    private VisitDetailDto mapToVisitDetail(VisitEntity visitEntity, List<VisitPositionEntity> visitPositionEntities) {
        VisitDetailDto visitDetail = modelMapper.map(visitEntity, VisitDetailDto.class);
        List<ServiceTypeDto> serviceTypes = getServiceTypes(visitPositionEntities);
        Time visitDuration = visitService.calculateVisitDuration(serviceTypes);
        Timestamp endTime = TimeHelper.getEndDate(visitEntity.getStartDate(), visitDuration);
        visitDetail.setEndDate(TimestampFormatParser.parse(endTime));
        return visitDetail;
    }

    private List<VisitPositionDetailDto> mapToVisitPositionDetails(List<VisitPositionEntity> visitPositionEntities) {
        List<ServiceTypeDto> serviceTypes = getServiceTypes(visitPositionEntities);
        DoctorDto doctor = getDoctorOrThrow(serviceTypes);
        return visitPositionEntities.stream()
                .map(vp -> {
                    ServiceTypeDetailDto serviceTypeDetail = mapToServiceTypeDetail(vp, serviceTypes, doctor);
                    return new VisitPositionDetailDto(vp.getId(), serviceTypeDetail);
                })
                .toList();
    }

    private DoctorDto getDoctorOrThrow(List<ServiceTypeDto> serviceTypes) {
        long doctorId = serviceTypes.stream().map(ServiceTypeDto::getDoctorId).findFirst()
                .orElseThrow(() -> new NotFoundException(String.format(DOCTOR_NOT_FOUND_MESSAGE, serviceTypes.stream().map(ServiceTypeDto::getId).toList())));
        return userClient.getDoctorById(doctorId);
    }

    private ServiceTypeDetailDto mapToServiceTypeDetail(VisitPositionEntity visitPosition, List<ServiceTypeDto> serviceTypes, DoctorDto doctor) {
        ServiceTypeDto serviceType = getServiceTypeOrThrow(serviceTypes, visitPosition.getServiceTypeId());
        ServiceTypeDetailDto serviceTypeDetail = modelMapper.map(serviceType, ServiceTypeDetailDto.class);
        serviceTypeDetail.setDoctor(doctor);
        return serviceTypeDetail;
    }

    private ServiceTypeDto getServiceTypeOrThrow(List<ServiceTypeDto> serviceTypes, Long serviceTypeId) {
        return serviceTypes.stream().filter(type -> type.getId().equals(serviceTypeId)).findFirst()
                .orElseThrow(() -> new NotFoundException(String.format(SERVICE_TYPE_NOT_FOUND_MESSAGE, serviceTypeId)));

    }
}
