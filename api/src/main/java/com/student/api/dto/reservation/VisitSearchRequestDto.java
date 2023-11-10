package com.student.api.dto.reservation;

import com.student.api.dto.common.PagingDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Valid
@NoArgsConstructor
public class VisitSearchRequestDto extends PagingDto {
    @NotNull
    private Long userId;
}
