package com.student.api.dto.user;

import com.student.api.annotation.validator.postalcode.PostalCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Valid
@NoArgsConstructor
public class AddressDto {
    @Schema(
            accessMode = Schema.AccessMode.READ_ONLY,
            description = "Id of address, autogenerated value, please ignore for POST operations"
    )
    private Long id;

    @NotNull
    private Long voivodeshipId;

    @NotNull
    @NotBlank
    @Size(min = 2, max = 50)
    private String city;

    @NotNull
    @NotBlank
    @PostalCode
    @Size(min = 6, max = 6)
    private String postalCode;

    @NotNull
    @NotBlank
    @Size(min = 2, max = 80)
    private String street;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 8)
    private String buildingNumber;
}
