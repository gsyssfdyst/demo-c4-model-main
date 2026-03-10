package com.biblioteca.service;

import com.biblioteca.model.Book;
import com.biblioteca.model.BookLoan;
import com.biblioteca.model.LoanStatus;
import com.biblioteca.model.User;
import com.biblioteca.repository.BookLoanRepository;
import com.biblioteca.repository.BookRepository;
import com.biblioteca.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class BookLoanService {

    @Autowired
    private BookLoanRepository bookLoanRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    public List<BookLoan> getAllLoans() {
        return bookLoanRepository.findAll();
    }

    public List<BookLoan> getLoansByUser(Long userId) {
        return bookLoanRepository.findByUserId(userId);
    }

    @Transactional
    public BookLoan rentBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (!book.isAvailable()) {
            throw new RuntimeException("Book is currently not available for rent");
        }

        // Create the loan with 14 days due date
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(java.util.Calendar.DAY_OF_MONTH, 14);
        Date dueDate = cal.getTime();

        BookLoan loan = new BookLoan(book, user, new Date(), dueDate, LoanStatus.ACTIVE);
        
        // Update book availability
        book.setAvailable(false);
        bookRepository.save(book);

        return bookLoanRepository.save(loan);
    }

    @Transactional
    public BookLoan returnBook(Long loanId) {
        BookLoan loan = bookLoanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));

        if (loan.getStatus() == LoanStatus.RETURNED) {
            throw new RuntimeException("Book is already returned");
        }

        loan.setStatus(LoanStatus.RETURNED);
        loan.setReturnDate(new Date());

        Book book = loan.getBook();
        book.setAvailable(true);
        bookRepository.save(book);

        return bookLoanRepository.save(loan);
    }
}
