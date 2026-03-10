package com.biblioteca.c4.common.dto;

import jakarta.validation.constraints.NotBlank;

public record FinePaymentRequestDTO(
        @NotBlank String paymentMethod
) {
}
