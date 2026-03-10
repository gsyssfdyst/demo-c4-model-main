package com.biblioteca.c4.common.dto;

import java.time.OffsetDateTime;

public record ErrorResponseDTO(
        OffsetDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {
}
