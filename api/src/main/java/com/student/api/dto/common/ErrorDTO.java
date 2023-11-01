package com.student.api.dto.common;

import lombok.*;

import java.util.Set;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDTO {
    private String message;
    private int status;
    private Set<String> reasons;
}
