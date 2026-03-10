package com.biblioteca.c4.common.dto;

import com.biblioteca.c4.domain.model.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LibraryUserCreateRequestDTO(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank String password,
        @NotNull UserRole role,
        boolean active
) {
}
