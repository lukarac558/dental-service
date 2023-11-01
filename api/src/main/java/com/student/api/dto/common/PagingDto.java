package com.student.api.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Map<String, Sort.Direction> sortMap = new HashMap<>();
    @NotNull
    private Long page = 0L;
    @NotNull
    private Long pageSize = 20L;
    @NotNull
    private Boolean enabled = true;

    @JsonIgnore
    public Pageable pageable() {
        return new PageRequest(page, pageSize, sortMap, enabled);
    }
}
