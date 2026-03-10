package com.biblioteca.presentation.controller;

import com.biblioteca.application.service.BookService;
import com.biblioteca.application.service.LoanService;
import com.biblioteca.application.service.LibraryUserService;
import com.biblioteca.common.dto.*;
import com.biblioteca.common.model.AuthUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/web/loans")
public class LoansWebController {

    private final LoanService loanService;
    private final LibraryUserService userService;
    private final BookService bookService;

    public LoansWebController(LoanService loanService, LibraryUserService userService, BookService bookService) {
        this.loanService = loanService;
        this.userService = userService;
        this.bookService = bookService;
    }

    @GetMapping
    public String listLoans(Model model, HttpSession session) {
        AuthUser authUser = (AuthUser) session.getAttribute("authUser");
        List<LoanDTO> loans = loanService.getAllLoans();
        List<LibraryUserDTO> users = userService.getAllUsers();
        List<BookDTO> availableBooks = bookService.getAllBooks().stream()
            .filter(BookDTO::getAvailable)
            .collect(Collectors.toList());
        
        model.addAttribute("loans", loans);
        model.addAttribute("users", users);
        model.addAttribute("availableBooks", availableBooks);
        model.addAttribute("authUser", authUser);
        model.addAttribute("page", "loans");
        model.addAttribute("borrow", new BorrowRequestDTO());
        
        return "loans/list";
    }

    @PostMapping("/borrow")
    public String borrowBook(BorrowRequestDTO dto, RedirectAttributes redirectAttributes, HttpSession session) {
        AuthUser authUser = (AuthUser) session.getAttribute("authUser");
        
        if (!authUser.isLibrarian()) {
            redirectAttributes.addFlashAttribute("error", "Apenas LIBRARIAN pode emprestar livros");
            return "redirect:/web/loans";
        }
        
        try {
            loanService.borrowBook(dto);
            redirectAttributes.addFlashAttribute("success", "Livro emprestado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao emprestar: " + e.getMessage());
        }
        
        return "redirect:/web/loans";
    }

    @PostMapping("/{id}/return")
    public String returnBook(@PathVariable Long id, RedirectAttributes redirectAttributes, HttpSession session) {
        AuthUser authUser = (AuthUser) session.getAttribute("authUser");
        
        if (!authUser.isLibrarian()) {
            redirectAttributes.addFlashAttribute("error", "Apenas LIBRARIAN pode devolver livros");
            return "redirect:/web/loans";
        }
        
        try {
            loanService.returnBook(id);
            redirectAttributes.addFlashAttribute("success", "Livro devolvido com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao devolver: " + e.getMessage());
        }
        
        return "redirect:/web/loans";
    }
}
