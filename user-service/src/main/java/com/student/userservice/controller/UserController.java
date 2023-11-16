package com.student.userservice.controller;

import com.student.api.annotation.extractor.auth.AuthInfo;
import com.student.api.annotation.extractor.auth.Info;
import com.student.api.dto.user.DoctorSearchRequestDto;
import com.student.api.dto.user.DoctorDto;
import com.student.api.dto.user.UserPersonalDetailsDto;
import com.student.userservice.entity.UserEntity;
import com.student.userservice.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/users")
@Tag(name = "Users")
@RequiredArgsConstructor
public class UserController {
    private final ModelMapper modelMapper;
    private final UserService userService;

    @GetMapping("")
    @ApiResponse(responseCode = "404", description = "User not found")
    @Operation(summary = "Find current user details information")
    public ResponseEntity<UserPersonalDetailsDto> getUser(
            @Parameter(hidden = true)
            @AuthInfo Info info
    ) {
        UserEntity user = userService.findUserByEmail(info.getEmail());
        return new ResponseEntity<>(modelMapper.map(user, UserPersonalDetailsDto.class), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiResponse(responseCode = "404", description = "User not found")
    @Operation(summary = "Find user by id.")
    public ResponseEntity<UserPersonalDetailsDto> getUserById(@PathVariable("id") Long id) {
        UserEntity user = userService.findUserById(id);
        return new ResponseEntity<>(modelMapper.map(user, UserPersonalDetailsDto.class), HttpStatus.OK);
    }

    @GetMapping("/doctor/{id}")
    @ApiResponse(responseCode = "404", description = "User not found")
    @Operation(summary = "Find doctors by id")
    public ResponseEntity<DoctorDto> getDoctorById(
            @PathVariable("id") Long id
    ) {
        return new ResponseEntity<>(
                modelMapper.map(userService.findDoctorById(id), DoctorDto.class),
                HttpStatus.OK
        );
    }

    @PostMapping("/doctors")
    @ApiResponse(responseCode = "404", description = "User not found")
    @Operation(summary = "Find doctors by provided search object")
    public ResponseEntity<Page<DoctorDto>> getDoctorsByRequest(
            @Validated
            @RequestBody
            DoctorSearchRequestDto doctorSearch
    ) {
        return new ResponseEntity<>(
                userService.findDoctor(doctorSearch)
                        .map(d -> modelMapper.map(d, DoctorDto.class)),
                HttpStatus.OK
        );
    }

    @PostMapping("")
    @Operation(summary = "Creates user")
    public ResponseEntity<UserPersonalDetailsDto> createUser(
            @Parameter(hidden = true)
            @AuthInfo Info info,
            @Parameter(required = true)
            @Validated
            @RequestBody UserPersonalDetailsDto userPersonalDetails
    ) {
        UserEntity user = userService.createIfNotPresent(info, modelMapper.map(userPersonalDetails, UserEntity.class));
        return new ResponseEntity<>(modelMapper.map(user, UserPersonalDetailsDto.class), HttpStatus.OK);
    }

    @PutMapping("")
    @Operation(summary = "Updates user")
    public ResponseEntity<UserPersonalDetailsDto> updateUser(
            @Parameter(hidden = true)
            @AuthInfo Info info,
            @Parameter(required = true)
            @Validated
            @RequestBody UserPersonalDetailsDto userPersonalDetails
    ) {
        UserEntity user = modelMapper.map(userPersonalDetails, UserEntity.class);
        return new ResponseEntity<>(
                modelMapper.map(
                        modelMapper.map(userService.updateUser(info, user), UserPersonalDetailsDto.class),
                        UserPersonalDetailsDto.class
                ),
                HttpStatus.OK
        );
    }

    @DeleteMapping("")
    @Operation(summary = "Delete user")
    public ResponseEntity<Void> deleteUser(
            @Parameter(hidden = true)
            @AuthInfo Info info
    ) {
        userService.deleteUser(info);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
