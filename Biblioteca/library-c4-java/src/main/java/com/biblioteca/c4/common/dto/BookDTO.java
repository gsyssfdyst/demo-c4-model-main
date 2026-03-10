package com.biblioteca.c4.common.dto;

import java.time.LocalDate;

public record BookDTO(
        Long id,
        String title,
        String author,
        String isbn,
        LocalDate publishedDate,
        boolean available
) {
}
