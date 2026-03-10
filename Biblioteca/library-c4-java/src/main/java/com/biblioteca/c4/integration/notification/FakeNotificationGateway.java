package com.biblioteca.c4.integration.notification;

import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FakeNotificationGateway implements NotificationGateway {

    private static final Logger LOGGER = LoggerFactory.getLogger(FakeNotificationGateway.class);

    @Override
    public void notifyBorrow(Long userId, Long loanId) {
        LOGGER.info("Notificacao simulada: emprestimo criado para userId={}, loanId={}", userId, loanId);
    }

    @Override
    public void notifyReturn(Long userId, Long loanId, BigDecimal fineAmount) {
        LOGGER.info("Notificacao simulada: devolucao userId={}, loanId={}, multa={}", userId, loanId, fineAmount);
    }

    @Override
    public void notifyFinePaid(Long userId, Long loanId, BigDecimal amount) {
        LOGGER.info("Notificacao simulada: multa paga userId={}, loanId={}, valor={}", userId, loanId, amount);
    }
}
