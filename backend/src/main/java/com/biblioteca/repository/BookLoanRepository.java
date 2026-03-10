package com.biblioteca.repository;

import com.biblioteca.model.BookLoan;
import com.biblioteca.model.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookLoanRepository extends JpaRepository<BookLoan, Long> {
    List<BookLoan> findByUserId(Long userId);
    List<BookLoan> findByBookId(Long bookId);
    Optional<BookLoan> findByBookIdAndStatus(Long bookId, LoanStatus status);
}
