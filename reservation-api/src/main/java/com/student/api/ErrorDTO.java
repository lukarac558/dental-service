package com.student.api;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDTO {
    private String message;
    private int status;
    private String reason;
}
