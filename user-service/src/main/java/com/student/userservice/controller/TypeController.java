package com.student.userservice.controller;

import com.student.api.annotation.extractor.auth.AuthInfo;
import com.student.api.annotation.extractor.auth.Info;
import com.student.api.dto.user.ServiceTypeDto;
import com.student.api.dto.user.ServiceTypeSearchRequestDto;
import com.student.userservice.entity.ServiceTypeEntity;
import com.student.userservice.service.type.TypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/service-type")
@Tag(name = "Service type")
@RequiredArgsConstructor
public class TypeController {
    private final ModelMapper modelMapper;
    private final TypeService serviceTypeService;

    @GetMapping("/{id}")
    @ApiResponse(responseCode = "404", description = "Service type not found")
    @Operation(summary = "Find service type by id.")
    public ResponseEntity<ServiceTypeDto> getTypeById(
            @PathVariable("id") Long id
    ) {
        return new ResponseEntity<>(
                modelMapper.map(serviceTypeService.findTypeById(id), ServiceTypeDto.class),
                HttpStatus.OK
        );
    }

    @PostMapping("/all")
    @Operation(summary = "Find all service types by provided search object")
    public ResponseEntity<Page<ServiceTypeDto>> getTypesByRequest(
            @Parameter(hidden = true)
            @AuthInfo Info info,
            @RequestBody ServiceTypeSearchRequestDto searchRequestDto
    ) {
        return new ResponseEntity<>(
                serviceTypeService.findAll(info, searchRequestDto)
                        .map(t -> modelMapper.map(t, ServiceTypeDto.class)),
                HttpStatus.OK
        );
    }

    @PostMapping("")
    @ApiResponse(responseCode = "404", description = "User not found")
    @Operation(summary = "Add new service type for given doctor.")
    public ResponseEntity<ServiceTypeDto> createServiceType(
            @Parameter(hidden = true)
            @AuthInfo Info info,
            @RequestBody ServiceTypeDto serviceType
    ) {
        ServiceTypeEntity type = serviceTypeService
                .createType(
                        modelMapper.map(serviceType, ServiceTypeEntity.class),
                        info
                );

        return new ResponseEntity<>(
                modelMapper.map(type, ServiceTypeDto.class),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @ApiResponse(responseCode = "404", description = "Service type not found")
    @Operation(summary = "Update service type by id.")
    public ResponseEntity<ServiceTypeDto> updateType(
            @Parameter(hidden = true)
            @AuthInfo Info info,
            @PathVariable("id") Long id,
            @Parameter(required = true)
            @RequestBody ServiceTypeDto serviceType

    ) {
        ServiceTypeEntity type = modelMapper.map(
          serviceType,
          ServiceTypeEntity.class
        );
        type.setId(id);

        return new ResponseEntity<>(
                modelMapper.map(
                        serviceTypeService.updateType(
                                type,
                                info
                        ),
                        ServiceTypeDto.class
                ),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete service type by id.")
    public ResponseEntity<Void> deleteType(
            @Parameter(hidden = true)
            @AuthInfo Info info,
            @PathVariable("id") Long id
    ) {
        serviceTypeService.deleteType(info, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
