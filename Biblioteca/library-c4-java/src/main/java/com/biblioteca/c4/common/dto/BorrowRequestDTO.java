package com.biblioteca.c4.common.dto;

import jakarta.validation.constraints.NotNull;

public record BorrowRequestDTO(
        @NotNull Long userId,
        @NotNull Long bookId
) {
}
