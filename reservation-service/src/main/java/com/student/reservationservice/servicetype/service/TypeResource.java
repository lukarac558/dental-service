package com.student.reservationservice.servicetype.service;

import com.student.api.ApplicationUserInfoDTO;
import com.student.api.ServiceTypeCreationDTO;
import com.student.api.ServiceTypeDTO;
import com.student.reservationservice.common.exception.entity.NotFoundException;
import com.student.reservationservice.common.utils.TimeFormatParser;
import com.student.reservationservice.servicetype.entity.ServiceType;
import com.student.reservationservice.user.applicationuser.entity.ApplicationUser;
import com.student.reservationservice.user.applicationuser.service.UserService;
import com.student.reservationservice.user.applicationuser.utils.ApplicationUserMapper;
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

import static com.student.reservationservice.common.exception.entity.ErrorConstants.SERVICE_TYPE_NOT_FOUND_MESSAGE;
import static com.student.reservationservice.common.exception.entity.ErrorConstants.USER_NOT_FOUND_MESSAGE;

@RestController
@RequestMapping("/service-type")
@Tag(name = "Service type")
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

    @GetMapping("/{id}")
    @ApiResponse(responseCode = "404", description = "Service type not found")
    @Operation(summary = "Find service type by id.")
    public ResponseEntity<ServiceTypeDTO> getTypeById(@PathVariable("id") Long id) {
        ServiceType type = typeService.findTypeById(id)
                .orElseThrow(() -> new NotFoundException(String.format(SERVICE_TYPE_NOT_FOUND_MESSAGE, id)));
        return new ResponseEntity<>(modelMapper.map(type, ServiceTypeDTO.class), HttpStatus.OK);
    }

    @GetMapping("/find-by/{user_id}")
    @Operation(summary = "Find all service types associated with a doctor by his id.")
    public ResponseEntity<List<ServiceTypeDTO>> getTypesByUserId(@PathVariable("user_id") Long userId) {
        List<ServiceType> serviceTypes = typeService.findServiceTypesByUserId(userId);
        List<ServiceTypeDTO> serviceTypesResponse = serviceTypes.stream().map(day -> modelMapper.map(day, ServiceTypeDTO.class)).toList();
        return new ResponseEntity<>(serviceTypesResponse, HttpStatus.OK);
    }

    @PostMapping("/")
    @ApiResponse(responseCode = "404", description = "User not found")
    @Operation(summary = "Add new service type for given doctor.")
    public ResponseEntity<ServiceTypeDTO> addType(@RequestBody ServiceTypeCreationDTO serviceTypeCreationDTO) {
        Long userId = serviceTypeCreationDTO.getUserId();
        ApplicationUser user = userService.findUserById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_MESSAGE, userId)));

        ServiceType type = typeService.addOrUpdateType(mapToServiceType(serviceTypeCreationDTO, user));

        ServiceTypeDTO serviceTypeDTO = mapToServiceTypeDTO(type, user);
        return new ResponseEntity<>(serviceTypeDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiResponse(responseCode = "404", description = "Service type not found")
    @Operation(summary = "Update service type by id.")
    public ResponseEntity<ServiceTypeDTO> updateType(@PathVariable("id") Long id,
                                                     @Parameter(required = true)
                                                     @RequestParam String name,
                                                     @RequestParam String description,
                                                     @Parameter(example = "01:00:00")
                                                     @RequestParam String durationTime) {
        ServiceType type = typeService.findTypeById(id)
                .map(t -> {
                    t.setName(name);
                    t.setDescription(description);
                    setDurationTimeOrThrow(t, durationTime);
                    return t;
                })
                .orElseThrow(() -> new NotFoundException(String.format(SERVICE_TYPE_NOT_FOUND_MESSAGE, id)));

        ServiceType updatedType = typeService.addOrUpdateType(type);
        return new ResponseEntity<>(modelMapper.map(updatedType, ServiceTypeDTO.class), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete service type by id.")
    public ResponseEntity<?> deleteType(@PathVariable("id") Long id) {
        typeService.deleteType(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private ServiceType mapToServiceType(ServiceTypeCreationDTO serviceTypeCreationDTO, ApplicationUser user) {
        ServiceType serviceType = new ServiceType();
        serviceType.setName(serviceTypeCreationDTO.getName());
        serviceType.setDescription(serviceTypeCreationDTO.getDescription());
        setDurationTimeOrThrow(serviceType, serviceTypeCreationDTO.getDurationTime());
        serviceType.setUser(user);
        return serviceType;
    }

    private void setDurationTimeOrThrow(ServiceType serviceType, String durationTime) {
        serviceType.setDurationTime(TimeFormatParser.parse(durationTime));
    }

    private ServiceTypeDTO mapToServiceTypeDTO(ServiceType serviceType, ApplicationUser applicationUser) {
        ServiceTypeDTO serviceTypeDTO = modelMapper.map(serviceType, ServiceTypeDTO.class);
        ApplicationUserInfoDTO applicationUserInfoDTO = ApplicationUserMapper.map(applicationUser);
        serviceTypeDTO.setUser(applicationUserInfoDTO);
        return serviceTypeDTO;
    }
}
