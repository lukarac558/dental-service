package com.student.userservice.client;

import com.student.api.dto.location.VoivodeshipDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("location-service")
public interface VoivodeshipClient {
    @GetMapping(value = "/voivodeships/{id}")
    VoivodeshipDto getVoivodeship(@PathVariable("id") Long id);
}
