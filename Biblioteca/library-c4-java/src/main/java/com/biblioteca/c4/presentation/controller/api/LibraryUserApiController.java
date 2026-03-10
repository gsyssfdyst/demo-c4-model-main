package com.biblioteca.c4.presentation.controller.api;

import com.biblioteca.c4.application.service.LibraryUserService;
import com.biblioteca.c4.common.dto.LibraryUserCreateRequestDTO;
import com.biblioteca.c4.common.dto.LibraryUserDTO;
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
@RequestMapping("/api/users")
@Authenticated
public class LibraryUserApiController {

    private final LibraryUserService libraryUserService;

    public LibraryUserApiController(LibraryUserService libraryUserService) {
        this.libraryUserService = libraryUserService;
    }

    @RoleAllowed({UserRole.ADMIN, UserRole.BIBLIOTECARIO})
    @GetMapping
    public ResponseEntity<List<LibraryUserDTO>> list() {
        return ResponseEntity.ok(libraryUserService.listAll());
    }

    @RoleAllowed({UserRole.ADMIN, UserRole.BIBLIOTECARIO})
    @GetMapping("/{id}")
    public ResponseEntity<LibraryUserDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(libraryUserService.getById(id));
    }

    @RoleAllowed(UserRole.ADMIN)
    @PostMapping
    public ResponseEntity<LibraryUserDTO> create(@Valid @RequestBody LibraryUserCreateRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryUserService.create(request));
    }

    @RoleAllowed(UserRole.ADMIN)
    @PutMapping("/{id}")
    public ResponseEntity<LibraryUserDTO> update(@PathVariable Long id,
                                                 @Valid @RequestBody LibraryUserCreateRequestDTO request) {
        return ResponseEntity.ok(libraryUserService.update(id, request));
    }

    @RoleAllowed(UserRole.ADMIN)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        libraryUserService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
