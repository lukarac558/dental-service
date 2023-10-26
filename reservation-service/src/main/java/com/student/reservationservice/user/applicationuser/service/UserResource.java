package com.student.reservationservice.user.applicationuser.service;

import com.student.api.ApplicationUserCreationDTO;
import com.student.api.ApplicationUserDTO;
import com.student.api.Role;
import com.student.reservationservice.user.address.city.entity.City;
import com.student.reservationservice.user.address.city.exception.CityNotFoundException;
import com.student.reservationservice.user.address.city.service.CityService;
import com.student.reservationservice.user.applicationuser.entity.ApplicationUser;
import com.student.reservationservice.user.applicationuser.exception.UserNotFoundException;
import com.student.reservationservice.user.competency.entity.CompetencyInformation;
import com.student.reservationservice.user.competency.exception.CompetencyNotFoundException;
import com.student.reservationservice.user.competency.service.CompetencyService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
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

    @GetMapping("/find/{id}")
    public ResponseEntity<ApplicationUserDTO> getUserById(@PathVariable("id") Long id) {
        ApplicationUser user = userService.findUserById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return new ResponseEntity<>(modelMapper.map(user, ApplicationUserDTO.class), HttpStatus.OK);
    }

    @GetMapping("/login")
    public ResponseEntity<ApplicationUserDTO> login(@RequestParam String email, @RequestParam String password) {
        ApplicationUser user = userService.login(email, password);
        return new ResponseEntity<>(modelMapper.map(user, ApplicationUserDTO.class), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<ApplicationUserDTO> register(@RequestBody ApplicationUserCreationDTO userCreationDTO) {
        String cityCode = userCreationDTO.getCityCode();
        City city = cityService.findCityByCode(cityCode)
                .orElseThrow(() -> new CityNotFoundException(cityCode));

        ApplicationUser addedUser = userService.register(mapToApplicationUser(userCreationDTO, city));
        return new ResponseEntity<>(modelMapper.map(addedUser, ApplicationUserDTO.class), HttpStatus.CREATED);
    }

    @PostMapping("/assign/competency-information")
    public ResponseEntity<ApplicationUserDTO> assignCompetencyInformation(@RequestParam Long userId, @RequestParam Long competencyInformationId) {
        ApplicationUser user = userService.findUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        CompetencyInformation competencyInformation = competencyService.findCompetencyInformationById(competencyInformationId)
                .orElseThrow(() -> new CompetencyNotFoundException(competencyInformationId));

        user.setCompetencyInformation(competencyInformation);
        ApplicationUser updatedUser = userService.assignCompetencyInformation(user);
        return new ResponseEntity<>(modelMapper.map(updatedUser, ApplicationUserDTO.class), HttpStatus.OK);
    }

    @DeleteMapping("/unassign/competency-information")
    public ResponseEntity<ApplicationUserDTO> assignCompetencyInformation(@RequestParam Long userId) {
        ApplicationUser user = userService.findUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        user.setCompetencyInformation(null);
        ApplicationUser updatedUser = userService.assignCompetencyInformation(user);
        return new ResponseEntity<>(modelMapper.map(updatedUser, ApplicationUserDTO.class), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private ApplicationUser mapToApplicationUser(ApplicationUserCreationDTO applicationUserCreationDTO, City city) {
        ApplicationUser user = modelMapper.map(applicationUserCreationDTO, ApplicationUser.class);
        user.setRole(Role.PATIENT);
        user.setCity(city);
        return user;
    }
}
