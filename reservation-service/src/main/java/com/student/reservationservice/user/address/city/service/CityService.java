package com.student.reservationservice.user.address.city.service;

import com.student.reservationservice.common.exception.entity.IncorrectFormatException;
import com.student.reservationservice.common.exception.entity.ObjectAlreadyExistsException;
import com.student.reservationservice.user.address.city.entity.City;
import com.student.reservationservice.user.address.city.repository.CityRepository;
import com.student.reservationservice.user.address.city.utils.CityCodeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.student.reservationservice.common.exception.entity.ErrorConstants.CITY_ALREADY_EXISTS_MESSAGE;
import static com.student.reservationservice.common.exception.entity.ErrorConstants.INCORRECT_CITY_CODE_FORMAT_MESSAGE;

@Service
public class CityService {
    private final CityRepository cityRepository;

    @Autowired
    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public City addOrCreateCity(City city) {
        validateCityCode(city.getCode());
        return cityRepository.save(city);
    }

    public List<City> findAllCities() {
        return cityRepository.findAll();
    }

    public Optional<City> findCityByCode(String code) {
        return cityRepository.findCityByCode(code);
    }

    public List<City> findCitiesByVoivodeshipId(int voivodeshipId) {
        return cityRepository.findCitiesByVoivodeshipId(voivodeshipId);
    }

    @Transactional
    public void deleteCity(String code) {
        cityRepository.deleteCityByCode(code);
    }

    private void validateCityCode(String code) {
        validateCodeFormatCorrectness(code);
        validateCodeUniqueness(code);
    }

    private void validateCodeFormatCorrectness(String code) {
        if (!CityCodeValidator.isValid(code)) {
            throw new IncorrectFormatException(String.format(INCORRECT_CITY_CODE_FORMAT_MESSAGE, code));
        }
    }

    private void validateCodeUniqueness(String code) {
        Optional<City> city = findCityByCode(code);
        city.ifPresent(c -> {
            throw new ObjectAlreadyExistsException(String.format(CITY_ALREADY_EXISTS_MESSAGE, code));
        });
    }
}
