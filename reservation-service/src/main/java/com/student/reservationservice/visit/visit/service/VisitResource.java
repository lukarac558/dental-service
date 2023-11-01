package com.student.reservationservice.visit.visit.service;

import com.student.api.dto.reservation.VisitCreationDTO;
import com.student.api.dto.reservation.VisitDTO;
import com.student.api.dto.user.UserPersonalDetailsDto;
import com.student.api.exception.NotFoundException;
import com.student.api.util.TimestampFormatParser;
import com.student.reservationservice.user.UserClient;
import com.student.reservationservice.visit.visit.entity.Visit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.student.api.exception.ErrorConstants.VISIT_NOT_FOUND_MESSAGE;

@RestController
@RequestMapping("/visit")
@Tag(name = "Visit")
@RequiredArgsConstructor
public class VisitResource {
    private final ModelMapper modelMapper;
    private final VisitService visitService;
    private final UserClient userClient;

    @GetMapping("/{id}")
    @ApiResponse(responseCode = "404", description = "Visit not found")
    @Operation(summary = "Find visit by id")
    public ResponseEntity<VisitDTO> getVisitById(@PathVariable("id") Long id) {
        Visit visit = visitService.findVisitById(id)
                .orElseThrow(() -> new NotFoundException(String.format(VISIT_NOT_FOUND_MESSAGE, id)));
        return new ResponseEntity<>(modelMapper.map(visit, VisitDTO.class), HttpStatus.OK);
    }

    @GetMapping("/find-by/{user_id}")
    @Operation(summary = "Find all patient visits by his id.")
    public ResponseEntity<List<VisitDTO>> getVisitsByUserId(@PathVariable("user_id") Long userId) {
        List<Visit> visits = visitService.findVisitsByUserId(userId);
        List<VisitDTO> visitsResponse = visits.stream().map(v -> modelMapper.map(v, VisitDTO.class)).toList();
        return new ResponseEntity<>(visitsResponse, HttpStatus.OK);
    }

    @PostMapping("/")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "422", description = "Incorrect timestamp format is given")
    @Operation(summary = "Add new visit for the patient.")
    public ResponseEntity<VisitDTO> addVisit(@RequestBody VisitCreationDTO visitCreationDTO) {
        Long userId = visitCreationDTO.getPatientId();
        UserPersonalDetailsDto user = userClient.getUserById(userId);

        Visit visit = visitService.addOrUpdateVisit(mapToVisit(visitCreationDTO, user.getId()));

        VisitDTO visitDTO = mapToVisitDTO(visit, user.getId());
        return new ResponseEntity<>(visitDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiResponse(responseCode = "404", description = "Visit not found")
    @Operation(summary = "Update visit description by id.")
    public ResponseEntity<VisitDTO> updateVisit(@PathVariable("id") Long id,
                                                @RequestParam String description) {
        Visit visit = visitService.findVisitById(id)
                .map(v -> {
                    v.setDescription(description);
                    return v;
                })
                .orElseThrow(() -> new NotFoundException(String.format(VISIT_NOT_FOUND_MESSAGE, id)));

        Visit updatedVisit = visitService.addOrUpdateVisit(visit);
        return new ResponseEntity<>(modelMapper.map(updatedVisit, VisitDTO.class), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "404", description = "Visit not found")
    @ApiResponse(responseCode = "422", description = "Visit cancellation is forbidden")
    @Operation(summary = "Delete visit by id.")
    public ResponseEntity<Void> deleteVisit(@PathVariable("id") Long id) {
        visitService.deleteVisit(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private Visit mapToVisit(VisitCreationDTO visitCreationDTO, Long patientId) {
        Visit visit = new Visit();
        setDatesOrThrow(visit, visitCreationDTO.getStartDate(), visitCreationDTO.getReservationDate());
        visit.setPatientId(patientId);
        return visit;
    }

    private void setDatesOrThrow(Visit visit, String startDate, String reservationDate) {
        visit.setStartDate(TimestampFormatParser.parse(startDate));
        visit.setReservationDate(TimestampFormatParser.parse(reservationDate));
    }

    private VisitDTO mapToVisitDTO(Visit visit, Long patientId) {
        VisitDTO visitDTO = modelMapper.map(visit, VisitDTO.class);
        visitDTO.setPatientId(patientId);
        return visitDTO;
    }
}
