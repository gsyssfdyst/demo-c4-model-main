package com.biblioteca.c4.application.service;

import com.biblioteca.c4.common.dto.DashboardDTO;
import com.biblioteca.c4.domain.model.enums.FinePaymentStatus;
import com.biblioteca.c4.domain.model.enums.LoanStatus;
import com.biblioteca.c4.persistence.repository.BookRepository;
import com.biblioteca.c4.persistence.repository.LibraryUserRepository;
import com.biblioteca.c4.persistence.repository.LoanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {

    private final BookRepository bookRepository;
    private final LibraryUserRepository libraryUserRepository;
    private final LoanRepository loanRepository;

    public DashboardService(BookRepository bookRepository,
                            LibraryUserRepository libraryUserRepository,
                            LoanRepository loanRepository) {
        this.bookRepository = bookRepository;
        this.libraryUserRepository = libraryUserRepository;
        this.loanRepository = loanRepository;
    }

    @Transactional(readOnly = true)
    public DashboardDTO getData() {
        long totalBooks = bookRepository.count();
        long availableBooks = bookRepository.countByAvailableTrue();
        long totalUsers = libraryUserRepository.countByActiveTrue();
        long activeLoans = loanRepository.countByStatus(LoanStatus.EMPRESTADO);
        long pendingFineLoans = loanRepository.countByFinePaymentStatus(FinePaymentStatus.PENDENTE);
        return new DashboardDTO(totalBooks, availableBooks, totalUsers, activeLoans, pendingFineLoans);
    }
}
