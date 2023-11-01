package com.student.api.dto.user;

import com.student.api.dto.common.PagingDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Valid
@Getter
@Setter
@NoArgsConstructor
public class DoctorSearchRequestDto extends PagingDto {
    @NotNull
    private String name = "";

    @NotNull
    private String service = "";
}
