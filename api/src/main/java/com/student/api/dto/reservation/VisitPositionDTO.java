package com.student.api.dto.reservation;

import lombok.Data;

@Data
public class VisitPositionDTO {
    private Long id;
    private VisitDTO visit;
    private Long serviceTypeId;
}
