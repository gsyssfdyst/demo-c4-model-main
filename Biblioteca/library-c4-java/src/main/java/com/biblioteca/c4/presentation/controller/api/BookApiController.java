package com.biblioteca.c4.presentation.controller.api;

import com.biblioteca.c4.application.service.BookService;
import com.biblioteca.c4.common.dto.BookCreateRequestDTO;
import com.biblioteca.c4.common.dto.BookDTO;
import com.biblioteca.c4.common.security.Authenticated;
import com.biblioteca.c4.common.security.RoleAllowed;
import com.biblioteca.c4.domain.model.enums.UserRole;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
@Authenticated
public class BookApiController {

    private final BookService bookService;

    public BookApiController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> list() {
        return ResponseEntity.ok(bookService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getById(id));
    }

    @RoleAllowed({UserRole.ADMIN, UserRole.BIBLIOTECARIO})
    @PostMapping
    public ResponseEntity<BookDTO> create(@Valid @RequestBody BookCreateRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.create(request));
    }

    @RoleAllowed({UserRole.ADMIN, UserRole.BIBLIOTECARIO})
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> update(@PathVariable Long id, @Valid @RequestBody BookCreateRequestDTO request) {
        return ResponseEntity.ok(bookService.update(id, request));
    }

    @RoleAllowed({UserRole.ADMIN, UserRole.BIBLIOTECARIO})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
