package com.student.reservationservice.controller;

import com.student.api.dto.reservation.VisitPositionDto;
import com.student.api.dto.user.ServiceTypeDto;
import com.student.api.exception.NotFoundException;
import com.student.reservationservice.service.VisitPositionService;
import com.student.reservationservice.user.UserClient;
import com.student.reservationservice.entity.VisitEntity;
import com.student.reservationservice.service.visit.VisitService;
import com.student.reservationservice.entity.VisitPositionEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.student.api.exception.handler.ErrorConstants.VISIT_POSITION_NOT_FOUND_MESSAGE;

@RestController
@RequestMapping("/visit-positions")
@Tag(name = "Visit positions")
@RequiredArgsConstructor
public class VisitPositionController {
    private final ModelMapper modelMapper;
    private final VisitPositionService visitPositionService;
    private final VisitService visitService;
    private final UserClient userClient;

    @GetMapping("/{id}")
    @ApiResponse(responseCode = "404", description = "Visit position not found")
    @Operation(summary = "Find visit position by id")
    public ResponseEntity<VisitPositionDto> getVisitPositionById(@PathVariable("id") Long id) {
        VisitPositionEntity visitPositionEntity = visitPositionService.findVisitPositionByVId(id)
                .orElseThrow(() -> new NotFoundException(String.format(VISIT_POSITION_NOT_FOUND_MESSAGE, id)));
        return new ResponseEntity<>(modelMapper.map(visitPositionEntity, VisitPositionDto.class), HttpStatus.OK);
    }

    @GetMapping("/visit/{visit_id}")
    @Operation(summary = "Find all positions for a given visit by its id.")
    public ResponseEntity<List<VisitPositionDto>> getVisitPositionsByVisitId(@PathVariable("visit_id") Long visitId) {
        List<VisitPositionEntity> visitPositionEntities = visitPositionService.findVisitPositionByVisitId(visitId);
        List<VisitPositionDto> visitPositionsResponse = visitPositionEntities.stream().map(v -> modelMapper.map(v, VisitPositionDto.class)).toList();
        return new ResponseEntity<>(visitPositionsResponse, HttpStatus.OK);
    }

    @PostMapping("")
    @ApiResponse(responseCode = "404", description = "Visit or service type not found")
    @Operation(summary = "Add new position for the visit.")
    public ResponseEntity<VisitPositionDto> addVisitPosition(@RequestParam Long visitId,
                                                             @RequestParam Long serviceTypeId) {
        VisitEntity visitEntity = getVisitEntityOrThrow(visitId);

        ServiceTypeDto serviceTypeDto = userClient.getServiceTypeById(serviceTypeId);

        VisitPositionEntity visitPositionEntity = visitPositionService.addVisitPosition(new VisitPositionEntity(visitEntity, serviceTypeDto.getId()));

        VisitPositionDto visitPositionDTO = modelMapper.map(visitPositionEntity, VisitPositionDto.class);
        return new ResponseEntity<>(visitPositionDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete visit position by id.")
    public ResponseEntity<Void> deleteVisitPosition(@PathVariable("id") Long id) {
        visitPositionService.deleteVisitPosition(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private VisitEntity getVisitEntityOrThrow(Long visitId) {
        return visitService.findVisitById(visitId)
                .orElseThrow(() -> new NotFoundException(String.format(VISIT_POSITION_NOT_FOUND_MESSAGE, visitId)));
    }
}
