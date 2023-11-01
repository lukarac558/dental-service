package com.student.reservationservice.user;

import com.student.api.dto.user.ServiceTypeDto;
import com.student.api.dto.user.UserPersonalDetailsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("user-service")
public interface UserClient {
    @GetMapping(value = "/user/{id}")
    UserPersonalDetailsDto getUserById(@PathVariable("id") Long id);

    @GetMapping(value = "/service-type/{id}")
    ServiceTypeDto getServiceTypeById(Long serviceTypeId);
}
