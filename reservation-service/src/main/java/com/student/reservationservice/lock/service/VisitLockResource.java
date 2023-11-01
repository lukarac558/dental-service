package com.student.reservationservice.lock.service;

import com.student.api.dto.reservation.VisitLockDTO;
import com.student.api.util.TimestampFormatParser;
import com.student.reservationservice.lock.entity.VisitLock;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
@RequestMapping("/visit-lock")
@Tag(name = "Visit lock")
public class VisitLockResource {
    private final ModelMapper modelMapper;
    private final VisitLockService visitLockService;

    @Autowired
    public VisitLockResource(ModelMapper modelMapper, VisitLockService visitLockService) {
        this.modelMapper = modelMapper;
        this.visitLockService = visitLockService;
    }

    @GetMapping("/")
    @ApiResponse(responseCode = "422", description = "Incorrect timestamp format is given")
    @Operation(summary = "Check if given date to doctor is currently temporarily booked.")
    public Boolean isVisitAvailability(@Schema(required = true, example = "2023-10-27 08:00:00")
                                       @RequestParam String startDate,
                                       @Schema(required = true)
                                       @RequestParam Long userId) {
        Timestamp parsedStartDate = getParsedTimestampOrThrow(startDate);
        return visitLockService.ifExistsVisitLockWithGivenUserIdAndStartDate(userId, parsedStartDate);
    }

    @PostMapping("/")
    @ApiResponse(responseCode = "409", description = "Visit lock already exists")
    @ApiResponse(responseCode = "422", description = "Incorrect timestamp format is given")
    @Operation(summary = "Put a lock for doctor's visit.")
    public ResponseEntity<VisitLockDTO> addVisitLock(@Schema(required = true, example = "2023-10-27 08:00:00")
                                                     @RequestParam String startDate,
                                                     @Schema(required = true)
                                                     @RequestParam Long userId) {
        Timestamp parsedStartDate = getParsedTimestampOrThrow(startDate);
        VisitLock newVisitLock = visitLockService.lock(new VisitLock(parsedStartDate, userId));
        return new ResponseEntity<>(modelMapper.map(newVisitLock, VisitLockDTO.class), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Release the lock for a doctor's visit.")
    public ResponseEntity<Void> deleteVisitLock(@PathVariable("id") Long id) {
        visitLockService.unlock(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private Timestamp getParsedTimestampOrThrow(String startDate) {
        return TimestampFormatParser.parse(startDate);
    }
}
