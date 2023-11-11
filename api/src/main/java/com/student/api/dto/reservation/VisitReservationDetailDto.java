package com.student.api.dto.reservation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Valid
@NoArgsConstructor
@AllArgsConstructor
public class VisitReservationDetailDto {
    @NotNull
    private VisitDto visitDto;

    @NotNull
    private List<VisitPositionDetailDto> visitPositionDetails;
}
