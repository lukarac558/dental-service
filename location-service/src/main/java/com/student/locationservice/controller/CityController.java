package com.student.locationservice.controller;

import com.student.api.dto.location.CityDto;

import com.student.api.dto.location.CitySearchRequestDto;
import com.student.locationservice.entity.CityEntity;
import com.student.locationservice.service.city.CityService;
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

@RestController
@RequestMapping("/cities")
@Tag(name = "City")
@RequiredArgsConstructor
public class CityController {

    private final ModelMapper modelMapper;
    private final CityService cityService;

    @PostMapping("")
    @ApiResponse(responseCode = "400", description = "Invalid request content")
    @Operation(summary = "Find all cities by provided search parameters")
    public ResponseEntity<Page<CityDto>> getAll(
            @Validated @RequestBody CitySearchRequestDto searchRequestDto
    ) {
        Page<CityEntity> cities = cityService.findAll(searchRequestDto);
        return new ResponseEntity<>(cities.map(c -> modelMapper.map(c, CityDto.class)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiResponse(responseCode = "404", description = "CityEntity not found")
    @Operation(summary = "Find city by code")
    public ResponseEntity<CityDto> getCityByCode(@PathVariable("id") Long id) {
        CityEntity city = cityService.findCityById(id);
        return new ResponseEntity<>(modelMapper.map(city, CityDto.class), HttpStatus.OK);
    }

}
