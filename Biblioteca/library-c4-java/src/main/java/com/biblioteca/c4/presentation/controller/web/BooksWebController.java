package com.biblioteca.c4.presentation.controller.web;

import com.biblioteca.c4.application.service.BookService;
import com.biblioteca.c4.common.dto.BookCreateRequestDTO;
import com.biblioteca.c4.common.security.Authenticated;
import com.biblioteca.c4.common.security.RoleAllowed;
import com.biblioteca.c4.domain.model.enums.UserRole;
import jakarta.validation.Valid;
import java.time.LocalDate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Authenticated
public class BooksWebController {

    private final BookService bookService;

    public BooksWebController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public String list(Model model) {
        model.addAttribute("title", "Livros");
        model.addAttribute("books", bookService.listAll());
        model.addAttribute("bookForm", new BookForm());
        return "books/list";
    }

    @RoleAllowed({UserRole.ADMIN, UserRole.BIBLIOTECARIO})
    @PostMapping("/books")
    public String create(@ModelAttribute("bookForm") @Valid BookForm form) {
        bookService.create(form.toRequest());
        return "redirect:/books";
    }

    @RoleAllowed({UserRole.ADMIN, UserRole.BIBLIOTECARIO})
    @PostMapping("/books/{id}/delete")
    public String delete(@PathVariable Long id) {
        bookService.delete(id);
        return "redirect:/books";
    }

    public static class BookForm {
        private String title;
        private String author;
        private String isbn;
        private LocalDate publishedDate;
        private boolean available = true;

        public BookCreateRequestDTO toRequest() {
            return new BookCreateRequestDTO(title, author, isbn, publishedDate, available);
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getIsbn() {
            return isbn;
        }

        public void setIsbn(String isbn) {
            this.isbn = isbn;
        }

        public LocalDate getPublishedDate() {
            return publishedDate;
        }

        public void setPublishedDate(LocalDate publishedDate) {
            this.publishedDate = publishedDate;
        }

        public boolean isAvailable() {
            return available;
        }

        public void setAvailable(boolean available) {
            this.available = available;
        }
    }
}
