package com.student.locationservice.service.city;

import com.student.api.exception.NotFoundException;
import com.student.api.dto.location.CitySearchRequestDto;
import com.student.locationservice.entity.CityEntity;
import com.student.locationservice.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import static com.student.api.exception.ErrorConstants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CityService {
    private final CityRepository cityRepository;

    public Page<CityEntity> findAll(CitySearchRequestDto citySearchRequestDto) {

        return cityRepository.findAll(
                new CitySpecification(citySearchRequestDto.getVoivodeshipIds(), citySearchRequestDto.getName()),
                citySearchRequestDto.pageable()
        );
    }

    public CityEntity findCityById(Long id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format(CITY_NOT_FOUND_MESSAGE, id)));
    }



}
