package com.student.reservationservice.visit.visit.service;

import com.student.api.ApplicationUserInfoDTO;
import com.student.api.VisitCreationDTO;
import com.student.api.VisitDTO;
import com.student.reservationservice.common.exception.entity.NotFoundException;
import com.student.reservationservice.common.utils.TimestampFormatParser;
import com.student.reservationservice.user.applicationuser.entity.ApplicationUser;
import com.student.reservationservice.user.applicationuser.service.UserService;
import com.student.reservationservice.user.applicationuser.utils.ApplicationUserMapper;
import com.student.reservationservice.visit.visit.entity.Visit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.student.reservationservice.common.exception.entity.ErrorConstants.USER_NOT_FOUND_MESSAGE;
import static com.student.reservationservice.common.exception.entity.ErrorConstants.VISIT_NOT_FOUND_MESSAGE;

@RestController
@RequestMapping("/visit")
@Tag(name = "Visit")
public class VisitResource {
    private final ModelMapper modelMapper;
    private final VisitService visitService;
    private final UserService userService;

    @Autowired
    public VisitResource(ModelMapper modelMapper, VisitService visitService, UserService userService) {
        this.modelMapper = modelMapper;
        this.visitService = visitService;
        this.userService = userService;
    }

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
        Long userId = visitCreationDTO.getUserId();
        ApplicationUser user = userService.findUserById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_MESSAGE, userId)));

        Visit visit = visitService.addOrUpdateVisit(mapToVisit(visitCreationDTO, user));

        VisitDTO visitDTO = mapToVisitDTO(visit, user);
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
    public ResponseEntity<?> deleteVisit(@PathVariable("id") Long id) {
        visitService.deleteVisit(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private Visit mapToVisit(VisitCreationDTO visitCreationDTO, ApplicationUser user) {
        Visit visit = new Visit();
        setDatesOrThrow(visit, visitCreationDTO.getStartDate(), visitCreationDTO.getReservationDate());
        visit.setUser(user);
        return visit;
    }

    private void setDatesOrThrow(Visit visit, String startDate, String reservationDate) {
        visit.setStartDate(TimestampFormatParser.parse(startDate));
        visit.setReservationDate(TimestampFormatParser.parse(reservationDate));
    }

    private VisitDTO mapToVisitDTO(Visit visit, ApplicationUser applicationUser) {
        VisitDTO visitDTO = modelMapper.map(visit, VisitDTO.class);
        ApplicationUserInfoDTO applicationUserInfoDTO = ApplicationUserMapper.map(applicationUser);
        visitDTO.setUser(applicationUserInfoDTO);
        return visitDTO;
    }
}
