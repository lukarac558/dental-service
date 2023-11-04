package com.student.api.annotation.extractor.auth;

import com.student.api.dto.common.enums.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public final class Info {
    private final String email;
    private final List<Role> roles;
}
