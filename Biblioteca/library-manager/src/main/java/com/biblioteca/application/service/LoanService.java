package com.biblioteca.application.service;

import com.biblioteca.common.dto.LoanDTO;
import com.biblioteca.common.dto.BorrowRequestDTO;
import com.biblioteca.common.exception.ResourceNotFoundException;
import com.biblioteca.common.exception.BookNotAvailableException;
import com.biblioteca.domain.model.Loan;
import com.biblioteca.domain.model.Book;
import com.biblioteca.domain.model.LibraryUser;
import com.biblioteca.persistence.repository.LoanRepository;
import com.biblioteca.persistence.repository.BookRepository;
import com.biblioteca.integration.notification.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final LibraryUserService userService;
    private final NotificationService notificationService;

    private static final long LOAN_DAYS = 14;
    private static final BigDecimal FINE_PER_DAY = new BigDecimal("2.00");

    public LoanService(LoanRepository loanRepository, BookRepository bookRepository, 
                       LibraryUserService userService, NotificationService notificationService) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    public LoanDTO borrowBook(BorrowRequestDTO dto) {
        LibraryUser user = userService.getUserEntityById(dto.getUserId());
        
        Book book = bookRepository.findById(dto.getBookId())
            .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + dto.getBookId()));
        
        if (!book.getAvailable()) {
            throw new BookNotAvailableException("Book is not available for borrowing");
        }
        
        LocalDate loanDate = LocalDate.now();
        LocalDate dueDate = loanDate.plusDays(LOAN_DAYS);
        
        Loan loan = new Loan(user, book, loanDate, dueDate);
        
        book.setAvailable(false);
        bookRepository.save(book);
        
        Loan savedLoan = loanRepository.save(loan);
        return convertToDTO(savedLoan);
    }

    public LoanDTO returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
            .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + loanId));
        
        LocalDate returnDate = LocalDate.now();
        loan.setReturnDate(returnDate);
        
        if (returnDate.isAfter(loan.getDueDate())) {
            long daysLate = ChronoUnit.DAYS.between(loan.getDueDate(), returnDate);
            BigDecimal fineAmount = FINE_PER_DAY.multiply(new BigDecimal(daysLate));
            loan.setFineAmount(fineAmount);
            
            notificationService.sendFineNotification(loan.getUser().getId(), loanId, fineAmount);
        } else {
            loan.setFineAmount(BigDecimal.ZERO);
        }
        
        Book book = loan.getBook();
        book.setAvailable(true);
        bookRepository.save(book);
        
        Loan returnedLoan = loanRepository.save(loan);
        return convertToDTO(returnedLoan);
    }

    @Transactional(readOnly = true)
    public List<LoanDTO> getAllLoans() {
        return loanRepository.findAll()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    private LoanDTO convertToDTO(Loan loan) {
        return new LoanDTO(
            loan.getId(),
            loan.getUser().getId(),
            loan.getBook().getId(),
            loan.getLoanDate(),
            loan.getDueDate(),
            loan.getReturnDate(),
            loan.getFineAmount()
        );
    }
}
