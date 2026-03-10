package com.biblioteca.presentation.controller;

import com.biblioteca.common.dto.LibraryUserDTO;
import com.biblioteca.application.service.LibraryUserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class LibraryUserController {

    private final LibraryUserService userService;

    public LibraryUserController(LibraryUserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<LibraryUserDTO> createUser(@Valid @RequestBody LibraryUserDTO dto) {
        LibraryUserDTO created = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<LibraryUserDTO>> getAllUsers() {
        List<LibraryUserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibraryUserDTO> getUserById(@PathVariable Long id) {
        LibraryUserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
}
