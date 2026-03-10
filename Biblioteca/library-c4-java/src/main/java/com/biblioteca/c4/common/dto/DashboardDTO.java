package com.biblioteca.c4.common.dto;

public record DashboardDTO(
        long totalBooks,
        long availableBooks,
        long totalUsers,
        long activeLoans,
        long pendingFineLoans
) {
}
