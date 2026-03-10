package com.biblioteca.c4.common.dto;

import com.biblioteca.c4.domain.model.enums.FinePaymentStatus;
import com.biblioteca.c4.domain.model.enums.LoanStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanDTO(
        Long id,
        Long bookId,
        String bookTitle,
        Long userId,
        String userName,
        LocalDate loanDate,
        LocalDate dueDate,
        LocalDate returnDate,
        BigDecimal fineAmount,
        FinePaymentStatus finePaymentStatus,
        LoanStatus status
) {
}
