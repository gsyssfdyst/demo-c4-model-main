package com.biblioteca.c4.presentation.controller.api;

import com.biblioteca.c4.application.service.LoanService;
import com.biblioteca.c4.common.dto.BorrowRequestDTO;
import com.biblioteca.c4.common.dto.FinePaymentRequestDTO;
import com.biblioteca.c4.common.dto.LoanDTO;
import com.biblioteca.c4.common.security.Authenticated;
import com.biblioteca.c4.common.security.RoleAllowed;
import com.biblioteca.c4.domain.model.enums.UserRole;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/loans")
@Authenticated
public class LoanApiController {

    private final LoanService loanService;

    public LoanApiController(LoanService loanService) {
        this.loanService = loanService;
    }

    @RoleAllowed({UserRole.ADMIN, UserRole.BIBLIOTECARIO})
    @GetMapping
    public ResponseEntity<List<LoanDTO>> list() {
        return ResponseEntity.ok(loanService.listAll());
    }

    @RoleAllowed({UserRole.ADMIN, UserRole.BIBLIOTECARIO})
    @GetMapping("/{id}")
    public ResponseEntity<LoanDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.getById(id));
    }

    @RoleAllowed({UserRole.ADMIN, UserRole.BIBLIOTECARIO})
    @PostMapping("/borrow")
    public ResponseEntity<LoanDTO> borrow(@Valid @RequestBody BorrowRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(loanService.borrow(request));
    }

    @RoleAllowed({UserRole.ADMIN, UserRole.BIBLIOTECARIO})
    @PostMapping("/{id}/return")
    public ResponseEntity<LoanDTO> returnBook(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.returnBook(id));
    }

    @RoleAllowed({UserRole.ADMIN, UserRole.BIBLIOTECARIO})
    @PostMapping("/{id}/pay-fine")
    public ResponseEntity<LoanDTO> payFine(@PathVariable Long id, @Valid @RequestBody FinePaymentRequestDTO request) {
        return ResponseEntity.ok(loanService.payFine(id, request.paymentMethod()));
    }
}
