package com.biblioteca.c4.integration.payment;

import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FakePaymentGateway implements PaymentGateway {

    private static final Logger LOGGER = LoggerFactory.getLogger(FakePaymentGateway.class);

    @Override
    public boolean processFinePayment(Long loanId, Long userId, BigDecimal amount, String paymentMethod) {
        LOGGER.info("Pagamento simulado: loanId={}, userId={}, amount={}, method={}", loanId, userId, amount, paymentMethod);
        return true;
    }
}
