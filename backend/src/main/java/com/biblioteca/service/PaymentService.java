package com.biblioteca.service;

import org.springframework.stereotype.Service;
import java.util.logging.Logger;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

@Service
public class PaymentService {
    private static final Logger logger = Logger.getLogger(PaymentService.class.getName());
    
    private final List<String> paymentLogs = new ArrayList<>();
    
    // Multa de R$ 2.00 por dia de atraso
    private static final double LATE_FEE_PER_DAY = 2.00;

    public double calculateLateFee(java.util.Date dueDate, java.util.Date returnDate) {
        if (returnDate.before(dueDate) || returnDate.equals(dueDate)) {
            return 0.0;
        }
        
        long diffInMillies = Math.abs(returnDate.getTime() - dueDate.getTime());
        long diffInDays = java.util.concurrent.TimeUnit.DAYS.convert(diffInMillies, java.util.concurrent.TimeUnit.MILLISECONDS);
        
        if (diffInDays == 0) diffInDays = 1;

        return diffInDays * LATE_FEE_PER_DAY;
    }

    public boolean processPayment(Long userId, double amount) {
        String log1 = "[MOCK PAYMENT] Conectando ao Gateway Externo...";
        String log2 = "[MOCK PAYMENT] Processando pagamento de R$ " + String.format("%.2f", amount) + " para o usuário de ID: " + userId;
        String log3 = "[MOCK PAYMENT] Transação Aprovada com Sucesso pela Operadora de Cartão!";
        
        logger.info(log1);
        logger.info(log2);
        logger.info(log3);
        
        paymentLogs.add(log1);
        paymentLogs.add(log2);
        paymentLogs.add(log3);
        
        return true;
    }

    public List<String> getPaymentLogs() {
        return Collections.unmodifiableList(paymentLogs);
    }
}
