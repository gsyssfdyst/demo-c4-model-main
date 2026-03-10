package com.biblioteca.c4.persistence.repository;

import com.biblioteca.c4.domain.model.Loan;
import com.biblioteca.c4.domain.model.enums.FinePaymentStatus;
import com.biblioteca.c4.domain.model.enums.LoanStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByStatus(LoanStatus status);

    long countByStatus(LoanStatus status);

    long countByFinePaymentStatus(FinePaymentStatus finePaymentStatus);
}
