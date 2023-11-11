package com.student.api.dto.reservation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Valid
@NoArgsConstructor
public class VisitReservationDto {
    @NotNull
    private VisitDto visit;

    @NotEmpty
    private List<Long> serviceTypeIds;

}
