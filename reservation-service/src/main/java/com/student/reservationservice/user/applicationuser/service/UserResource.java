package com.student.reservationservice.user.applicationuser.service;

import com.student.api.ApplicationUserCreationDTO;
import com.student.api.ApplicationUserDTO;
import com.student.api.Role;
import com.student.reservationservice.common.exception.entity.NotFoundException;
import com.student.reservationservice.user.address.city.entity.City;
import com.student.reservationservice.user.address.city.service.CityService;
import com.student.reservationservice.user.applicationuser.entity.ApplicationUser;
import com.student.reservationservice.user.competency.entity.CompetencyInformation;
import com.student.reservationservice.user.competency.service.CompetencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.student.reservationservice.common.exception.entity.ErrorConstants.*;

@RestController
@RequestMapping("/user")
@Tag(name = "User")
public class UserResource {
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final CompetencyService competencyService;
    private final CityService cityService;

    @Autowired
    public UserResource(ModelMapper modelMapper, UserService userService, CompetencyService competencyService, CityService cityService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.competencyService = competencyService;
        this.cityService = cityService;
    }

    @GetMapping("/{id}")
    @ApiResponse(responseCode = "404", description = "User not found")
    @Operation(summary = "Find application user by id.")
    public ResponseEntity<ApplicationUserDTO> getUserById(@PathVariable("id") Long id) {
        ApplicationUser user = userService.findUserById(id)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_MESSAGE, id)));
        return new ResponseEntity<>(modelMapper.map(user, ApplicationUserDTO.class), HttpStatus.OK);
    }

    @GetMapping("/")
    @Operation(summary = "Get application user if login details are correct.")
    public ResponseEntity<ApplicationUserDTO> login(@RequestParam String email, @RequestParam String password) {
        ApplicationUser user = userService.login(email, password);
        return new ResponseEntity<>(modelMapper.map(user, ApplicationUserDTO.class), HttpStatus.OK);
    }

    @PostMapping("/")
    @ApiResponse(responseCode = "404", description = "City not found")
    @ApiResponse(responseCode = "409", description = "User with given email already exists")
    @ApiResponse(responseCode = "422", description = "Given email or pesel format is incorrect")
    @Operation(summary = "Register new application user.")
    public ResponseEntity<ApplicationUserDTO> register(@RequestBody ApplicationUserCreationDTO userCreationDTO) {
        String cityCode = userCreationDTO.getCityCode();
        City city = cityService.findCityByCode(cityCode)
                .orElseThrow(() -> new NotFoundException(String.format(CITY_NOT_FOUND_MESSAGE, cityCode)));

        ApplicationUser addedUser = userService.register(mapToApplicationUser(userCreationDTO, city));
        return new ResponseEntity<>(modelMapper.map(addedUser, ApplicationUserDTO.class), HttpStatus.CREATED);
    }

    @PostMapping("/assign/competency-information")
    @ApiResponse(responseCode = "404", description = "User or competency information not found")
    @Operation(summary = "Assign competency information to a given doctor.")
    public ResponseEntity<ApplicationUserDTO> assignCompetencyInformation(@RequestParam Long userId, @RequestParam Long competencyInformationId) {
        ApplicationUser user = userService.findUserById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_MESSAGE, userId)));

        CompetencyInformation competencyInformation = competencyService.findCompetencyInformationById(competencyInformationId)
                .orElseThrow(() -> new NotFoundException(String.format(COMPETENCY_INFORMATION_NOT_FOUND_MESSAGE, competencyInformationId)));

        user.setCompetencyInformation(competencyInformation);
        ApplicationUser updatedUser = userService.assignCompetencyInformation(user);
        return new ResponseEntity<>(modelMapper.map(updatedUser, ApplicationUserDTO.class), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete application user by id.")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/unassign/competency-information")
    @ApiResponse(responseCode = "404", description = "User not found")
    @Operation(summary = "Unassign competency information from a doctor by its id.")
    public ResponseEntity<ApplicationUserDTO> assignCompetencyInformation(@RequestParam Long userId) {
        ApplicationUser user = userService.findUserById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_MESSAGE, userId)));

        user.setCompetencyInformation(null);
        ApplicationUser updatedUser = userService.assignCompetencyInformation(user);
        return new ResponseEntity<>(modelMapper.map(updatedUser, ApplicationUserDTO.class), HttpStatus.NO_CONTENT);
    }

    private ApplicationUser mapToApplicationUser(ApplicationUserCreationDTO applicationUserCreationDTO, City city) {
        ApplicationUser user = modelMapper.map(applicationUserCreationDTO, ApplicationUser.class);
        user.setRole(Role.PATIENT);
        user.setCity(city);
        return user;
    }
}
