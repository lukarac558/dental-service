package com.student.reservationservice.user.address.city.service;

import com.student.api.CityCreationDTO;
import com.student.api.CityDTO;
import com.student.reservationservice.common.exception.entity.NotFoundException;
import com.student.reservationservice.user.address.city.entity.City;
import com.student.reservationservice.user.address.voivodeship.entity.Voivodeship;
import com.student.reservationservice.user.address.voivodeship.service.VoivodeshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.student.reservationservice.common.exception.entity.ErrorConstants.CITY_NOT_FOUND_MESSAGE;
import static com.student.reservationservice.common.exception.entity.ErrorConstants.VOIVODESHIP_ID_NOT_FOUND_MESSAGE;

@RestController
@RequestMapping("/city")
@Tag(name = "City")
public class CityResource {

    private final ModelMapper modelMapper;
    private final CityService cityService;
    private final VoivodeshipService voivodeshipService;

    @Autowired
    public CityResource(CityService cityService, VoivodeshipService voivodeshipService, ModelMapper modelMapper) {
        this.cityService = cityService;
        this.voivodeshipService = voivodeshipService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    @Operation(summary = "Find all cities.")
    public ResponseEntity<List<CityDTO>> getAllCities() {
        List<City> cities = cityService.findAllCities();
        List<CityDTO> citiesResponse = cities.stream().map(city -> modelMapper.map(city, CityDTO.class)).toList();
        return new ResponseEntity<>(citiesResponse, HttpStatus.OK);
    }

    @GetMapping("/find-by/{voivodeship_id}")
    @Operation(summary = "Find all cities in a given voivodeship by its id.")
    public ResponseEntity<List<CityDTO>> getAllCitiesByVoivodeshipId(@PathVariable("voivodeship_id") int voivodeshipId) {
        List<City> cities = cityService.findCitiesByVoivodeshipId(voivodeshipId);
        List<CityDTO> citiesResponse = cities.stream().map(city -> modelMapper.map(city, CityDTO.class)).toList();
        return new ResponseEntity<>(citiesResponse, HttpStatus.OK);
    }

    @GetMapping("/{code}")
    @ApiResponse(responseCode = "404", description = "City not found")
    @Operation(summary = "Find city by code")
    public ResponseEntity<CityDTO> getCityByCode(@PathVariable("code") String code) {
        City city = cityService.findCityByCode(code)
                .orElseThrow(() -> new NotFoundException(String.format(CITY_NOT_FOUND_MESSAGE, code)));
        return new ResponseEntity<>(modelMapper.map(city, CityDTO.class), HttpStatus.OK);
    }

    @PostMapping("/")
    @ApiResponse(responseCode = "404", description = "Voivodeship not found")
    @ApiResponse(responseCode = "409", description = "City with given code already exists")
    @ApiResponse(responseCode = "442", description = "Given city code format is incorrect")
    @Operation(summary = "Add new city.")
    public ResponseEntity<CityDTO> addCity(@RequestBody CityCreationDTO cityCreationDTO) {
        int voivodeshipId = cityCreationDTO.getVoivodeshipId();
        Voivodeship voivodeship = voivodeshipService.findVoivodeshipById(voivodeshipId)
                .orElseThrow(() -> new NotFoundException(String.format(VOIVODESHIP_ID_NOT_FOUND_MESSAGE, voivodeshipId)));

        City newCity = cityService.addOrCreateCity(mapToCity(cityCreationDTO, voivodeship));
        return new ResponseEntity<>(modelMapper.map(newCity, CityDTO.class), HttpStatus.CREATED);
    }

    @PutMapping("/{code}")
    @ApiResponse(responseCode = "404", description = "Voivodeship or city not found")
    @ApiResponse(responseCode = "409", description = "City with given code already exists")
    @ApiResponse(responseCode = "442", description = "Given city code format is incorrect")
    @Operation(summary = "Update city by code.")
    public ResponseEntity<CityDTO> updateCity(@PathVariable("code") String cityCode,
                                              @Parameter(required = true)
                                              @RequestParam String name,
                                              @Parameter(required = true)
                                              @RequestParam int voivodeshipId) {
        Voivodeship voivodeship = voivodeshipService.findVoivodeshipById(voivodeshipId)
                .orElseThrow(() -> new NotFoundException(String.format(VOIVODESHIP_ID_NOT_FOUND_MESSAGE, voivodeshipId)));

        City city = cityService.findCityByCode(cityCode)
                .map(c -> {
                    c.setName(name);
                    c.setVoivodeship(voivodeship);
                    return c;
                })
                .orElseThrow(() -> new NotFoundException(String.format(CITY_NOT_FOUND_MESSAGE, cityCode)));

        City updatedCity = cityService.addOrCreateCity(city);
        return new ResponseEntity<>(modelMapper.map(updatedCity, CityDTO.class), HttpStatus.OK);
    }

    @DeleteMapping("/{code}")
    @Operation(summary = "Delete city by id.")
    public ResponseEntity<?> deleteCity(@PathVariable("code") String code) {
        cityService.deleteCity(code);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private City mapToCity(CityCreationDTO cityCreationDTO, Voivodeship voivodeship) {
        City city = modelMapper.map(cityCreationDTO, City.class);
        city.setVoivodeship(voivodeship);
        return city;
    }
}
