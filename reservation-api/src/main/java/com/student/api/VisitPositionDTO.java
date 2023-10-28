package com.student.api;

import lombok.Data;

@Data
public class VisitPositionDTO {
    private Long id;
    private VisitDTO visit;
    private ServiceTypeDTO serviceType;
}
