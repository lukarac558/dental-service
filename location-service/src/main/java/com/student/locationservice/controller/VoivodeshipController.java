package com.student.locationservice.controller;

import com.student.api.dto.location.VoivodeshipDto;
import com.student.locationservice.entity.VoivodeshipEntity;
import com.student.locationservice.service.VoivodeshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/voivodeships")
@Tag(name = "Voivodeship")
@RequiredArgsConstructor
public class VoivodeshipController {
    private final ModelMapper modelMapper;
    private final VoivodeshipService voivodeshipService;

    @GetMapping("/{id}")
    @Operation(summary = "Find all available voivodeships.")
    public ResponseEntity<VoivodeshipDto> getVoivodeship(
            @PathVariable("id") Long id
    ) {
        VoivodeshipEntity voivodeship = voivodeshipService.findById(id);
        return new ResponseEntity<>(modelMapper.map(voivodeship, VoivodeshipDto.class), HttpStatus.OK);
    }

    @GetMapping("")
    @Operation(summary = "Find all available voivodeships.")
    public ResponseEntity<List<VoivodeshipDto>> getAllVoivodeships() {
        List<VoivodeshipEntity> voivodeships = voivodeshipService.findAllVoivodeships();
        List<VoivodeshipDto> voivodeshipsResponse = voivodeships.stream().map(v -> modelMapper.map(v, VoivodeshipDto.class)).toList();
        return new ResponseEntity<>(voivodeshipsResponse, HttpStatus.OK);
    }

}
