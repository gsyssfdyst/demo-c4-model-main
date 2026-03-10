package com.biblioteca.c4.common.model;

import com.biblioteca.c4.domain.model.enums.UserRole;

public record AuthUser(
        Long id,
        String name,
        String email,
        UserRole role
) {
}
