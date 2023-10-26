package com.student.reservationservice.servicetype.service;

import com.student.api.ApplicationUserInfoDTO;
import com.student.api.CalendarDayDTO;
import com.student.api.ServiceTypeCreationDTO;
import com.student.api.ServiceTypeDTO;
import com.student.reservationservice.calendar.entity.CalendarDay;
import com.student.reservationservice.servicetype.entity.ServiceType;
import com.student.reservationservice.servicetype.exception.ServiceTypeNotFoundException;
import com.student.reservationservice.user.applicationuser.entity.ApplicationUser;
import com.student.reservationservice.user.applicationuser.exception.UserNotFoundException;
import com.student.reservationservice.user.applicationuser.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.util.List;

@RestController
@RequestMapping("/service-type")
public class TypeResource {
    private final ModelMapper modelMapper;
    private final TypeService typeService;
    private final UserService userService;

    @Autowired
    public TypeResource(ModelMapper modelMapper, TypeService typeService, UserService userService) {
        this.modelMapper = modelMapper;
        this.typeService = typeService;
        this.userService = userService;
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<ServiceTypeDTO> getServiceTypeById(@PathVariable("id") Long id) {
        ServiceType type = typeService.findTypeById(id)
                .orElseThrow(() -> new ServiceTypeNotFoundException(id));
        return new ResponseEntity<>(modelMapper.map(type, ServiceTypeDTO.class), HttpStatus.OK);
    }

    @GetMapping("/find-by/{user_id}")
    public ResponseEntity<List<ServiceTypeDTO>> getCalendarDaysByUserId(@PathVariable("user_id") Long userId) {
        List<ServiceType> serviceTypes = typeService.findServiceTypesByUserId(userId);
        List<ServiceTypeDTO> serviceTypesResponse = serviceTypes.stream().map(day -> modelMapper.map(day, ServiceTypeDTO.class)).toList();
        return new ResponseEntity<>(serviceTypesResponse, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ServiceTypeDTO> addType(@RequestBody ServiceTypeCreationDTO serviceTypeCreationDTO) {
        Long userId = serviceTypeCreationDTO.getUserId();
        ApplicationUser user = userService.findUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        ServiceType type = typeService.addOrUpdateType(mapToServiceType(serviceTypeCreationDTO, user));

        ServiceTypeDTO serviceTypeDTO = mapToServiceTypeDTO(type, user);
        return new ResponseEntity<>(serviceTypeDTO, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ServiceTypeDTO> updateType(@PathVariable("id") Long id,
                                                     @RequestParam String name,
                                                     @RequestParam String description,
                                                     @RequestParam String durationTime) {
        ServiceType type = typeService.findTypeById(id)
                .map(t -> {
                    t.setName(name);
                    t.setDescription(description);
                    t.setDurationTime(Time.valueOf(durationTime));
                    return t;
                })
                .orElseThrow(() -> new ServiceTypeNotFoundException(id));

        ServiceType updatedType = typeService.addOrUpdateType(type);
        return new ResponseEntity<>(modelMapper.map(updatedType, ServiceTypeDTO.class), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteType(@PathVariable("id") Long id) {
        typeService.deleteType(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private ServiceType mapToServiceType(ServiceTypeCreationDTO serviceTypeCreationDTO, ApplicationUser user) {
        ServiceType serviceType = modelMapper.map(serviceTypeCreationDTO, ServiceType.class);
        serviceType.setId(null);
        serviceType.setUser(user);
        return serviceType;
    }

    private ServiceTypeDTO mapToServiceTypeDTO(ServiceType serviceType, ApplicationUser applicationUser) {
        ServiceTypeDTO serviceTypeDTO = modelMapper.map(serviceType, ServiceTypeDTO.class);
        ApplicationUserInfoDTO applicationUserInfoDTO = new ApplicationUserInfoDTO(
                applicationUser.getId(),
                applicationUser.getEmail(),
                applicationUser.getName(),
                applicationUser.getSurname(),
                applicationUser.getPhoneNumber()
        );

        serviceTypeDTO.setUser(applicationUserInfoDTO);
        return serviceTypeDTO;
    }
}
