package com.student.reservationservice.visit.visit.service;

import com.student.api.ApplicationUserInfoDTO;
import com.student.api.VisitCreationDTO;
import com.student.api.VisitDTO;
import com.student.reservationservice.user.applicationuser.entity.ApplicationUser;
import com.student.reservationservice.user.applicationuser.exception.UserNotFoundException;
import com.student.reservationservice.user.applicationuser.service.UserService;
import com.student.reservationservice.visit.visit.entity.Visit;
import com.student.reservationservice.visit.visit.exception.VisitNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/visit")
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

    @GetMapping("/find/{id}")
    public ResponseEntity<VisitDTO> getVisitById(@PathVariable("id") Long id) {
        Visit visit = visitService.findVisitById(id)
                .orElseThrow(() -> new VisitNotFoundException(id));
        return new ResponseEntity<>(modelMapper.map(visit, VisitDTO.class), HttpStatus.OK);
    }

    @GetMapping("/find-by/{user_id}")
    public ResponseEntity<List<VisitDTO>> getVisitsByUserId(@PathVariable("user_id") Long userId) {
        List<Visit> visits = visitService.findVisitsByUserId(userId);
        List<VisitDTO> visitsResponse = visits.stream().map(day -> modelMapper.map(day, VisitDTO.class)).toList();
        return new ResponseEntity<>(visitsResponse, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<VisitDTO> addVisit(@RequestBody VisitCreationDTO visitCreationDTO) {
        Long userId = visitCreationDTO.getUserId();
        ApplicationUser user = userService.findUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Visit visit = visitService.addOrUpdateVisit(mapToVisit(visitCreationDTO, user));

        VisitDTO visitDTO = mapToVisitDTO(visit, user);
        return new ResponseEntity<>(visitDTO, HttpStatus.CREATED);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<VisitDTO> updateVisit(@PathVariable("id") Long id,
                                                @RequestParam String description) {
        Visit visit = visitService.findVisitById(id)
                .map(v -> {
                    v.setDescription(description);
                    return v;
                })
                .orElseThrow(() -> new VisitNotFoundException(id));

        Visit updatedVisit = visitService.addOrUpdateVisit(visit);
        return new ResponseEntity<>(modelMapper.map(updatedVisit, VisitDTO.class), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVisit(@PathVariable("id") Long id) {
        visitService.deleteVisit(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private Visit mapToVisit(VisitCreationDTO visitCreationDTO, ApplicationUser user) {
        Visit visit = modelMapper.map(visitCreationDTO, Visit.class);
        visit.setId(null);
        visit.setUser(user);
        return visit;
    }

    private VisitDTO mapToVisitDTO(Visit visit, ApplicationUser applicationUser) {
        VisitDTO calendarDayDTO = modelMapper.map(visit, VisitDTO.class);
        ApplicationUserInfoDTO applicationUserInfoDTO = new ApplicationUserInfoDTO(
                applicationUser.getId(),
                applicationUser.getEmail(),
                applicationUser.getName(),
                applicationUser.getSurname(),
                applicationUser.getPhoneNumber()
        );

        calendarDayDTO.setUser(applicationUserInfoDTO);
        return calendarDayDTO;
    }
}
