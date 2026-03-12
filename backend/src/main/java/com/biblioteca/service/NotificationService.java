package com.biblioteca.service;

import org.springframework.stereotype.Service;
import java.util.logging.Logger;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

@Service
public class NotificationService {
    private static final Logger logger = Logger.getLogger(NotificationService.class.getName());

    private final List<String> notificationLogs = new ArrayList<>();

    public void sendLoanConfirmation(String userEmail, String bookTitle, java.util.Date dueDate) {
        String msg = "[MOCK NOTIFICATION] Enviando E-mail/SMS para " + userEmail + ": Empréstimo do livro '" + bookTitle + "' confirmado. Devolução até " + dueDate;
        logger.info(msg);
        notificationLogs.add(msg);
    }

    public void sendReturnConfirmation(String userEmail, String bookTitle) {
        String msg = "[MOCK NOTIFICATION] Enviando E-mail/SMS para " + userEmail + ": Devolução do livro '" + bookTitle + "' recebida com sucesso. Obrigado!";
        logger.info(msg);
        notificationLogs.add(msg);
    }

    public void sendLateFeeWarning(String userEmail, double amount) {
        String msg = "[MOCK NOTIFICATION] Enviando E-mail/SMS para " + userEmail + ": Aviso de Multa gerado pelo atraso! Valor: R$ " + String.format("%.2f", amount);
        logger.info(msg);
        notificationLogs.add(msg);
    }

    public List<String> getNotificationLogs() {
        return Collections.unmodifiableList(notificationLogs);
    }
}
