package com.biblioteca.c4.common.dto;

import com.biblioteca.c4.domain.model.enums.UserRole;

public record LoginResponseDTO(
        Long userId,
        String name,
        String email,
        UserRole role,
        String message
) {
}
