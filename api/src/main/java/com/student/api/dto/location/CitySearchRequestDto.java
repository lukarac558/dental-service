package com.student.api.dto.location;

import com.student.api.dto.common.PagingDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Valid
@NoArgsConstructor
public class CitySearchRequestDto extends PagingDto {
    @NotNull
    private Set<Long> voivodeshipIds = new HashSet<>();
    @NotNull
    private String name = "";
}
