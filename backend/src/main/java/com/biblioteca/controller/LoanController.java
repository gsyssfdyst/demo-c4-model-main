package com.biblioteca.controller;

import com.biblioteca.model.BookLoan;
import com.biblioteca.service.BookLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@CrossOrigin(origins = "*")
public class LoanController {

    @Autowired
    private BookLoanService bookLoanService;

    @GetMapping
    public List<BookLoan> getAllLoans() {
        return bookLoanService.getAllLoans();
    }

    @GetMapping("/user/{userId}")
    public List<BookLoan> getUserLoans(@PathVariable Long userId) {
        return bookLoanService.getLoansByUser(userId);
    }

    @PostMapping("/rent")
    public ResponseEntity<?> rentBook(@RequestParam Long userId, @RequestParam Long bookId) {
        try {
            BookLoan loan = bookLoanService.rentBook(userId, bookId);
            return ResponseEntity.ok(loan);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/return/{loanId}")
    public ResponseEntity<?> returnBook(@PathVariable Long loanId) {
        try {
            BookLoan loan = bookLoanService.returnBook(loanId);
            return ResponseEntity.ok(loan);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
