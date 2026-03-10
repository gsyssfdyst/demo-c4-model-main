package com.biblioteca.c4.integration.notification;

import java.math.BigDecimal;

public interface NotificationGateway {
    void notifyBorrow(Long userId, Long loanId);

    void notifyReturn(Long userId, Long loanId, BigDecimal fineAmount);

    void notifyFinePaid(Long userId, Long loanId, BigDecimal amount);
}
