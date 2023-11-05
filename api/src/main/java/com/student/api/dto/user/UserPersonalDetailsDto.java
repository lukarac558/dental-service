package com.student.api.dto.user;

import com.student.api.annotation.validator.phone.Phone;
import com.student.api.dto.common.enums.Sex;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.pl.PESEL;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Valid
@NoArgsConstructor
public class UserPersonalDetailsDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull
    @PESEL
    @Size(min = 11, max = 11)
    private String personalId;

    @NotNull
    @NotBlank
    @Size(min = 2, max = 50)
    private String name;

    @NotNull
    @NotBlank
    @Size(min = 2, max = 50)
    private String surname;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Sex sex;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String birthDate;

    @NotNull
    @NotBlank
    @Phone
    @Size(min = 8, max = 12)
    private String phoneNumber;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String email;

    @NotNull
    private AddressDto address;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Set<String> roles;
}
