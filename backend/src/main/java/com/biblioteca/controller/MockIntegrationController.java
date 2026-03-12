package com.biblioteca.controller;

import com.biblioteca.service.NotificationService;
import com.biblioteca.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/integrations")
@CrossOrigin(origins = "*")
public class MockIntegrationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/notifications")
    public ResponseEntity<List<String>> getNotifications() {
        return ResponseEntity.ok(notificationService.getNotificationLogs());
    }

    @GetMapping("/payments")
    public ResponseEntity<List<String>> getPayments() {
        return ResponseEntity.ok(paymentService.getPaymentLogs());
    }

    @GetMapping("/overview")
    public ResponseEntity<Map<String, Object>> getOverview() {
        Map<String, Object> response = new HashMap<>();
        response.put("notifications", notificationService.getNotificationLogs());
        response.put("payments", paymentService.getPaymentLogs());
        return ResponseEntity.ok(response);
    }
}
