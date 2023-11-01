package com.student.api.dto.common;

import java.util.Arrays;

public enum Role {
    DOCTOR("doctor"),
    PATIENT("patient");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public static Role fromRoleName(String roleName) {
        return Arrays.stream(Role.values())
                .filter(role -> role.getRoleName().equals(roleName))
                .findFirst().orElse(null);
    }
}
