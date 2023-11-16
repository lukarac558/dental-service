package com.student.locationservice.controller;

import com.student.api.dto.location.CityDto;
import com.student.api.dto.location.CitySearchRequestDto;
import com.student.api.dto.location.VoivodeshipDto;
import com.student.api.exception.NotFoundException;
import io.restassured.RestAssured;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CityControllerTest {
    @LocalServerPort
    private Integer port;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:16-alpine"
    );

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    CityController cityController;
    static final Long EXISTING_VOIVODESHIP_ID = 77L;
    static final Long EXISTING_CITY_ID = 7777L;
    static final String EXISTING_CITY_NAME = "Kalinowo";
    static final Long TOTAL_CITIES_COUNT_CURRENTLY_IN_DB = 5L;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Transactional
    @Test
    void getCityById_shouldFind() {
        ResponseEntity<CityDto> responseCity = cityController.getCityById(EXISTING_CITY_ID);
        int statusCode = responseCity.getStatusCode().value();

        CityDto city = Objects.requireNonNull(responseCity.getBody());
        VoivodeshipDto voivodeship = city.getVoivodeship();

        assertEquals(HttpStatus.OK.value(), statusCode);
        assertEquals(EXISTING_CITY_ID, city.getId());
        assertEquals("88-230", city.getCode());
        assertEquals("Piotrków Kujawski", city.getName());
        assertEquals(EXISTING_VOIVODESHIP_ID, voivodeship.getId());
        assertEquals("Kujawsko-Pomorskie", voivodeship.getName());
    }

    @Test
    void getCityById_shouldNotFind_throwNotFoundException() {
        assertThrows(NotFoundException.class, () -> {
            cityController.getCityById(9999L);
        });
    }

    @Transactional
    @Test
    void getCitiesByRequest_withGivenName_shouldFindOne() {
        CitySearchRequestDto searchRequest = getSearchRequest_withGivenName(EXISTING_CITY_NAME);
        ResponseEntity<Page<CityDto>> citiesResponse = cityController.getCitiesByRequest(searchRequest);

        int statusCode = citiesResponse.getStatusCode().value();
        List<CityDto> cities = Objects.requireNonNull(citiesResponse.getBody()).getContent();
        int citiesCount = cities.size();
        String name = cities.get(0).getName();

        assertEquals(HttpStatus.OK.value(), statusCode);
        assertEquals(1, citiesCount);
        assertEquals(EXISTING_CITY_NAME, name);
    }

    @Transactional
    @Test
    void getCitiesByRequest_withGivenVoivodeshipIds_shouldFindOne() {
        Set<Long> voivodeshipIds = Set.of(EXISTING_VOIVODESHIP_ID);
        CitySearchRequestDto searchRequest = getSearchRequest_withGivenVoivodeshipIds(voivodeshipIds);
        ResponseEntity<Page<CityDto>> citiesResponse = cityController.getCitiesByRequest(searchRequest);

        int statusCode = citiesResponse.getStatusCode().value();
        List<CityDto> cities = Objects.requireNonNull(citiesResponse.getBody()).getContent();
        Long id = cities.get(0).getId();

        assertEquals(HttpStatus.OK.value(), statusCode);
        assertEquals(1, cities.size());
        assertEquals(EXISTING_CITY_ID, id);
    }

    @Transactional
    @Test
    void getCitiesByRequest_withoutGivenSearchParameters_shouldFindFive() {
        ResponseEntity<Page<CityDto>> citiesResponse = cityController.getCitiesByRequest(new CitySearchRequestDto());

        int statusCode = citiesResponse.getStatusCode().value();
        List<CityDto> cities = Objects.requireNonNull(citiesResponse.getBody()).getContent();

        assertEquals(HttpStatus.OK.value(), statusCode);
        assertEquals(TOTAL_CITIES_COUNT_CURRENTLY_IN_DB, cities.size());
    }

    private CitySearchRequestDto getSearchRequest_withGivenName(String name) {
        CitySearchRequestDto searchRequest = new CitySearchRequestDto();
        searchRequest.setName(name);
        return searchRequest;
    }

    private CitySearchRequestDto getSearchRequest_withGivenVoivodeshipIds(Set<Long> voivodeshipIds) {
        CitySearchRequestDto searchRequest = new CitySearchRequestDto();
        searchRequest.setVoivodeshipIds(voivodeshipIds);
        return searchRequest;
    }
}
