package com.biblioteca.c4.presentation.controller.web;

import com.biblioteca.c4.application.service.BookService;
import com.biblioteca.c4.application.service.LibraryUserService;
import com.biblioteca.c4.application.service.LoanService;
import com.biblioteca.c4.common.dto.BorrowRequestDTO;
import com.biblioteca.c4.common.security.Authenticated;
import com.biblioteca.c4.common.security.RoleAllowed;
import com.biblioteca.c4.domain.model.enums.UserRole;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Authenticated
public class LoansWebController {

    private final LoanService loanService;
    private final BookService bookService;
    private final LibraryUserService libraryUserService;

    public LoansWebController(LoanService loanService,
                              BookService bookService,
                              LibraryUserService libraryUserService) {
        this.loanService = loanService;
        this.bookService = bookService;
        this.libraryUserService = libraryUserService;
    }

    @RoleAllowed({UserRole.ADMIN, UserRole.BIBLIOTECARIO})
    @GetMapping("/loans")
    public String list(Model model) {
        model.addAttribute("title", "Emprestimos");
        model.addAttribute("loans", loanService.listAll());
        model.addAttribute("borrowForm", new BorrowForm());
        model.addAttribute("users", libraryUserService.listAll());
        model.addAttribute("books", bookService.listAll().stream().filter(book -> book.available()).toList());
        return "loans/list";
    }

    @RoleAllowed({UserRole.ADMIN, UserRole.BIBLIOTECARIO})
    @PostMapping("/loans/borrow")
    public String borrow(@ModelAttribute("borrowForm") @Valid BorrowForm form) {
        loanService.borrow(new BorrowRequestDTO(form.getUserId(), form.getBookId()));
        return "redirect:/loans";
    }

    @RoleAllowed({UserRole.ADMIN, UserRole.BIBLIOTECARIO})
    @PostMapping("/loans/{id}/return")
    public String returnBook(@PathVariable Long id) {
        loanService.returnBook(id);
        return "redirect:/loans";
    }

    @RoleAllowed({UserRole.ADMIN, UserRole.BIBLIOTECARIO})
    @PostMapping("/loans/{id}/pay")
    public String payFine(@PathVariable Long id) {
        loanService.payFine(id, "WEB");
        return "redirect:/loans";
    }

    public static class BorrowForm {
        private Long userId;
        private Long bookId;

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getBookId() {
            return bookId;
        }

        public void setBookId(Long bookId) {
            this.bookId = bookId;
        }
    }
}
