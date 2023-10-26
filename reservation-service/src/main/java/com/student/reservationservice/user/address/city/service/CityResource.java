package com.student.reservationservice.user.address.city.service;

import com.student.api.CityCreationDTO;
import com.student.api.CityDTO;
import com.student.reservationservice.user.address.city.entity.City;
import com.student.reservationservice.user.address.city.exception.CityNotFoundException;
import com.student.reservationservice.user.address.voivodeship.entity.Voivodeship;
import com.student.reservationservice.user.address.voivodeship.exception.VoivodeshipNotFoundException;
import com.student.reservationservice.user.address.voivodeship.service.VoivodeshipService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/city")
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
    public ResponseEntity<List<CityDTO>> getAllCities() {
        List<City> cities = cityService.findAllCities();
        List<CityDTO> citiesResponse = cities.stream().map(city -> modelMapper.map(city, CityDTO.class)).toList();
        return new ResponseEntity<>(citiesResponse, HttpStatus.OK);
    }

    @GetMapping("/find-by/{voivodeship_id}")
    public ResponseEntity<List<CityDTO>> getAllCitiesByVoivodeshipId(@PathVariable("voivodeship_id") int voivodeshipId) {
        List<City> cities = cityService.findCitiesByVoivodeshipId(voivodeshipId);
        List<CityDTO> citiesResponse = cities.stream().map(city -> modelMapper.map(city, CityDTO.class)).toList();
        return new ResponseEntity<>(citiesResponse, HttpStatus.OK);
    }

    @GetMapping("/find-by-code/{code}")
    public ResponseEntity<CityDTO> getCityByCode(@PathVariable("code") String code) {
        City city = cityService.findCityByCode(code)
                .orElseThrow(() -> new CityNotFoundException(code));
        return new ResponseEntity<>(modelMapper.map(city, CityDTO.class), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<CityDTO> addCity(@RequestBody CityCreationDTO cityCreationDTO) {
        int voivodeshipId = cityCreationDTO.getVoivodeshipId();
        Voivodeship voivodeship = voivodeshipService.findVoivodeshipById(voivodeshipId)
                .orElseThrow(() -> new VoivodeshipNotFoundException(voivodeshipId));

        City newCity = cityService.addOrCreateCity(mapToCity(cityCreationDTO, voivodeship));
        return new ResponseEntity<>(modelMapper.map(newCity, CityDTO.class), HttpStatus.CREATED);
    }

    @PutMapping("/update/{cityCode}")
    public ResponseEntity<CityDTO> updateCity(@PathVariable("cityCode") String cityCode,
                                              @RequestParam String name,
                                              @RequestParam int voivodeshipId) {
        Voivodeship voivodeship = voivodeshipService.findVoivodeshipById(voivodeshipId)
                .orElseThrow(() -> new VoivodeshipNotFoundException(voivodeshipId));

        City city = cityService.findCityByCode(cityCode)
                .map(c -> {
                    c.setName(name);
                    c.setVoivodeship(voivodeship);
                    return c;
                })
                .orElseThrow(() -> new CityNotFoundException(cityCode));

        City updatedCity = cityService.addOrCreateCity(city);
        return new ResponseEntity<>(modelMapper.map(updatedCity, CityDTO.class), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{code}")
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
