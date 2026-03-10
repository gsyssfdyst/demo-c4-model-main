package com.biblioteca.c4.application.service;

import com.biblioteca.c4.common.dto.BorrowRequestDTO;
import com.biblioteca.c4.common.dto.LoanDTO;
import com.biblioteca.c4.common.exception.BusinessRuleException;
import com.biblioteca.c4.common.exception.ResourceNotFoundException;
import com.biblioteca.c4.domain.model.Book;
import com.biblioteca.c4.domain.model.LibraryUser;
import com.biblioteca.c4.domain.model.Loan;
import com.biblioteca.c4.domain.model.enums.FinePaymentStatus;
import com.biblioteca.c4.domain.model.enums.LoanStatus;
import com.biblioteca.c4.integration.notification.NotificationGateway;
import com.biblioteca.c4.integration.payment.PaymentGateway;
import com.biblioteca.c4.persistence.repository.BookRepository;
import com.biblioteca.c4.persistence.repository.LibraryUserRepository;
import com.biblioteca.c4.persistence.repository.LoanRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final LibraryUserRepository libraryUserRepository;
    private final NotificationGateway notificationGateway;
    private final PaymentGateway paymentGateway;
    private final int defaultLoanDays;
    private final BigDecimal finePerDay;

    public LoanService(LoanRepository loanRepository,
                       BookRepository bookRepository,
                       LibraryUserRepository libraryUserRepository,
                       NotificationGateway notificationGateway,
                       PaymentGateway paymentGateway,
                       @Value("${app.loan.default-days:14}") int defaultLoanDays,
                       @Value("${app.loan.fine-per-day:5.00}") BigDecimal finePerDay) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.libraryUserRepository = libraryUserRepository;
        this.notificationGateway = notificationGateway;
        this.paymentGateway = paymentGateway;
        this.defaultLoanDays = defaultLoanDays;
        this.finePerDay = finePerDay;
    }

    @Transactional(readOnly = true)
    public List<LoanDTO> listAll() {
        return loanRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public LoanDTO getById(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Emprestimo nao encontrado: " + id));
        return toDTO(loan);
    }

    @Transactional
    public LoanDTO borrow(BorrowRequestDTO request) {
        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new ResourceNotFoundException("Livro nao encontrado: " + request.bookId()));

        if (!book.isAvailable()) {
            throw new BusinessRuleException("Livro indisponivel para emprestimo");
        }

        LibraryUser user = libraryUserRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado: " + request.userId()));

        if (!user.isActive()) {
            throw new BusinessRuleException("Usuario inativo nao pode emprestar livros");
        }

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setUser(user);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(defaultLoanDays));
        loan.setStatus(LoanStatus.EMPRESTADO);
        loan.setFineAmount(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        loan.setFinePaymentStatus(FinePaymentStatus.SEM_MULTA);

        book.setAvailable(false);
        bookRepository.save(book);

        Loan savedLoan = loanRepository.save(loan);
        notificationGateway.notifyBorrow(user.getId(), savedLoan.getId());
        return toDTO(savedLoan);
    }

    @Transactional
    public LoanDTO returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Emprestimo nao encontrado: " + loanId));

        if (loan.getStatus() == LoanStatus.DEVOLVIDO) {
            throw new BusinessRuleException("Emprestimo ja foi devolvido");
        }

        LocalDate returnDate = LocalDate.now();
        loan.setReturnDate(returnDate);
        loan.setStatus(LoanStatus.DEVOLVIDO);

        BigDecimal fineAmount = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        if (returnDate.isAfter(loan.getDueDate())) {
            long lateDays = ChronoUnit.DAYS.between(loan.getDueDate(), returnDate);
            fineAmount = finePerDay
                    .multiply(BigDecimal.valueOf(lateDays))
                    .setScale(2, RoundingMode.HALF_UP);
            loan.setFinePaymentStatus(FinePaymentStatus.PENDENTE);
        } else {
            loan.setFinePaymentStatus(FinePaymentStatus.SEM_MULTA);
        }

        loan.setFineAmount(fineAmount);

        Book book = loan.getBook();
        book.setAvailable(true);
        bookRepository.save(book);

        Loan savedLoan = loanRepository.save(loan);
        notificationGateway.notifyReturn(savedLoan.getUser().getId(), savedLoan.getId(), savedLoan.getFineAmount());
        return toDTO(savedLoan);
    }

    @Transactional
    public LoanDTO payFine(Long loanId, String paymentMethod) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Emprestimo nao encontrado: " + loanId));

        if (loan.getFinePaymentStatus() != FinePaymentStatus.PENDENTE) {
            throw new BusinessRuleException("Emprestimo sem multa pendente para pagamento");
        }

        boolean paid = paymentGateway.processFinePayment(
                loan.getId(),
                loan.getUser().getId(),
                loan.getFineAmount(),
                paymentMethod
        );

        if (!paid) {
            throw new BusinessRuleException("Falha ao processar pagamento da multa");
        }

        loan.setFinePaymentStatus(FinePaymentStatus.PAGA);
        Loan savedLoan = loanRepository.save(loan);
        notificationGateway.notifyFinePaid(savedLoan.getUser().getId(), savedLoan.getId(), savedLoan.getFineAmount());
        return toDTO(savedLoan);
    }

    public LoanDTO toDTO(Loan loan) {
        return new LoanDTO(
                loan.getId(),
                loan.getBook().getId(),
                loan.getBook().getTitle(),
                loan.getUser().getId(),
                loan.getUser().getName(),
                loan.getLoanDate(),
                loan.getDueDate(),
                loan.getReturnDate(),
                loan.getFineAmount(),
                loan.getFinePaymentStatus(),
                loan.getStatus()
        );
    }
}
