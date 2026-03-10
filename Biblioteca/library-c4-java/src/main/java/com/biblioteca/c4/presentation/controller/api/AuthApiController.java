package com.biblioteca.c4.presentation.controller.api;

import com.biblioteca.c4.application.service.AuthService;
import com.biblioteca.c4.common.dto.LoginRequestDTO;
import com.biblioteca.c4.common.dto.LoginResponseDTO;
import com.biblioteca.c4.common.model.AuthUser;
import com.biblioteca.c4.common.security.Authenticated;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

    private final AuthService authService;

    public AuthApiController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request, HttpSession session) {
        return ResponseEntity.ok(authService.login(request, session));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        authService.logout(session);
        return ResponseEntity.noContent().build();
    }

    @Authenticated
    @GetMapping("/me")
    public ResponseEntity<AuthUser> me(HttpSession session) {
        return ResponseEntity.ok(authService.getCurrentUser(session));
    }
}
