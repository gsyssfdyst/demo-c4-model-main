package com.biblioteca.integration.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    public boolean sendFineNotification(Long userId, Long loanId, BigDecimal fineAmount) {
        logger.info("=== FineNotification ===");
        logger.info("UserId: {}", userId);
        logger.info("LoanId: {}", loanId);
        logger.info("FineAmount: R$ {}", fineAmount);
        logger.info("Message: Late return detected. A fine of R$ {} has been applied.", fineAmount);
        logger.info("======================");
        
        return true;
    }
}
