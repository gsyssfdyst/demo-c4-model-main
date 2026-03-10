package com.biblioteca.c4.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record BookCreateRequestDTO(
        @NotBlank String title,
        @NotBlank String author,
        @NotBlank String isbn,
        @NotNull LocalDate publishedDate,
        boolean available
) {
}
