package com.biblioteca.c4.integration.payment;

import java.math.BigDecimal;

public interface PaymentGateway {
    boolean processFinePayment(Long loanId, Long userId, BigDecimal amount, String paymentMethod);
}
