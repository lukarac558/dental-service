package com.student.reservationservice.lock.service;

import com.student.api.VisitLockCreationDTO;
import com.student.api.VisitLockDTO;
import com.student.reservationservice.lock.entity.VisitLock;
import com.student.reservationservice.user.address.city.exception.CityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/visit-lock")
public class VisitLockResource {
    private final ModelMapper modelMapper;
    private final VisitLockService visitLockService;

    @Autowired
    public VisitLockResource(ModelMapper modelMapper, VisitLockService visitLockService) {
        this.modelMapper = modelMapper;
        this.visitLockService = visitLockService;
    }

    @GetMapping("/check-availability")
    public Boolean isVisitAvailability(VisitLockCreationDTO visitLockCreationDTO) {
      return visitLockService.ifExistsVisitLockWithGivenUserIdAndStartDate(visitLockCreationDTO.getUserId(), visitLockCreationDTO.getStartDate());
    }

    // When someone before
    @PostMapping("/lock")
    public ResponseEntity<VisitLockDTO> addVisitLock(@RequestBody VisitLockCreationDTO visitLockCreationDTO) {
        VisitLock visitLock = modelMapper.map(visitLockCreationDTO, VisitLock.class);
        visitLock.setId(null);
        VisitLock newVisitLock = visitLockService.lock(visitLock);
        return new ResponseEntity<>(modelMapper.map(newVisitLock, VisitLockDTO.class), HttpStatus.CREATED);
    }

    @DeleteMapping("/unlock/{id}")
    public ResponseEntity<?> deleteVisitLock(@PathVariable("id") Long id) {
        visitLockService.unlock(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
