package com.biblioteca.presentation.controller;

import com.biblioteca.application.service.BookService;
import com.biblioteca.common.dto.BookDTO;
import com.biblioteca.common.model.AuthUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/web/books")
public class BooksWebController {

    private final BookService bookService;

    public BooksWebController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public String listBooks(Model model, HttpSession session) {
        AuthUser authUser = (AuthUser) session.getAttribute("authUser");
        List<BookDTO> books = bookService.getAllBooks();
        
        model.addAttribute("books", books);
        model.addAttribute("authUser", authUser);
        model.addAttribute("page", "books");
        
        return "books/list";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable Long id, Model model, HttpSession session) {
        AuthUser authUser = (AuthUser) session.getAttribute("authUser");
        
        if (!authUser.isLibrarian()) {
            return "redirect:/web/books?error=forbidden";
        }
        
        BookDTO book = bookService.getBookById(id);
        model.addAttribute("book", book);
        model.addAttribute("authUser", authUser);
        model.addAttribute("page", "books");
        
        return "books/edit";
    }
}
