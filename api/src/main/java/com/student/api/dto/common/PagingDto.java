package com.student.api.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Valid
@NoArgsConstructor
public class PagingDto {
    @NotNull
    @Schema(
            description = "Allows sorting by provided name of field and direction(ASC,DESC)",
            example = "{\"id\": \"ASC\"}",
            defaultValue = "{}"
    )
    private Map<String, Sort.Direction> sortMap = new HashMap<>();
    @NotNull
    @Schema(defaultValue = "0", example = "0")
    private Long page = 0L;
    @NotNull
    @Schema(defaultValue = "20", example = "20")
    private Long pageSize = 20L;
    @NotNull
    @Schema(defaultValue = "true", example = "true")
    private Boolean enabled = true;

    @JsonIgnore
    public Pageable pageable() {
        return new PageRequestDto(page, pageSize, sortMap, enabled);
    }
}
