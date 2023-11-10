package com.student.reservationservice.user;

import com.student.api.dto.user.ServiceTypeDto;
import com.student.api.dto.user.ServiceTypeSearchRequestDto;
import com.student.api.dto.user.UserPersonalDetailsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("user-service")
public interface UserClient {
    @GetMapping(value = "/users/{id}")
    UserPersonalDetailsDto getUserById(@PathVariable("id") Long id);

    @GetMapping(value = "/service-types/{id}")
    ServiceTypeDto getServiceTypeById(Long serviceTypeId);
    @GetMapping(value = "/service-types/")
    List<ServiceTypeDto> getServiceTypes(@RequestParam List<Long> ids);

    @GetMapping(value = "/service-types/doctor/{doctor_id}")
    List<ServiceTypeDto> getServiceTypesByDoctorId(@PathVariable("doctor_id") Long id);
}
