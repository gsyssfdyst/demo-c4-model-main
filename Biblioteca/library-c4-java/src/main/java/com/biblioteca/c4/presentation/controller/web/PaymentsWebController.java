package com.biblioteca.c4.presentation.controller.web;

import com.biblioteca.c4.application.service.LoanService;
import com.biblioteca.c4.common.security.Authenticated;
import com.biblioteca.c4.common.security.RoleAllowed;
import com.biblioteca.c4.domain.model.enums.FinePaymentStatus;
import com.biblioteca.c4.domain.model.enums.UserRole;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Authenticated
public class PaymentsWebController {

    private final LoanService loanService;

    public PaymentsWebController(LoanService loanService) {
        this.loanService = loanService;
    }

    @RoleAllowed({UserRole.ADMIN, UserRole.BIBLIOTECARIO})
    @GetMapping("/payments")
    public String listPendingPayments(Model model) {
        model.addAttribute("title", "Pagamentos");
        model.addAttribute("pendingLoans", loanService.listAll().stream()
                .filter(loan -> loan.finePaymentStatus() == FinePaymentStatus.PENDENTE)
                .toList());
        return "payments/list";
    }
}
