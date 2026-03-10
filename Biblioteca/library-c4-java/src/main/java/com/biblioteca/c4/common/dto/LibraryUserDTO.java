package com.biblioteca.c4.common.dto;

import com.biblioteca.c4.domain.model.enums.UserRole;

public record LibraryUserDTO(
        Long id,
        String name,
        String email,
        UserRole role,
        boolean active
) {
}
