package com.biblioteca.presentation.controller;

import com.biblioteca.common.dto.LoanDTO;
import com.biblioteca.common.dto.BorrowRequestDTO;
import com.biblioteca.common.dto.ReturnLoanRequestDTO;
import com.biblioteca.application.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/borrow")
    public ResponseEntity<LoanDTO> borrowBook(@Valid @RequestBody BorrowRequestDTO dto) {
        LoanDTO loan = loanService.borrowBook(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(loan);
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<LoanDTO> returnBook(@PathVariable Long id) {
        LoanDTO loan = loanService.returnBook(id);
        return ResponseEntity.ok(loan);
    }

    @GetMapping
    public ResponseEntity<List<LoanDTO>> getAllLoans() {
        List<LoanDTO> loans = loanService.getAllLoans();
        return ResponseEntity.ok(loans);
    }
}
