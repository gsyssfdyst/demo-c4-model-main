package com.biblioteca.c4.application.service;

import com.biblioteca.c4.common.dto.LoginRequestDTO;
import com.biblioteca.c4.common.dto.LoginResponseDTO;
import com.biblioteca.c4.common.exception.UnauthorizedException;
import com.biblioteca.c4.common.model.AuthUser;
import com.biblioteca.c4.domain.model.LibraryUser;
import com.biblioteca.c4.persistence.repository.LibraryUserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final LibraryUserRepository libraryUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final String authSessionKey;

    public AuthService(LibraryUserRepository libraryUserRepository,
                       PasswordEncoder passwordEncoder,
                       @Value("${app.auth.session-key:AUTH_USER}") String authSessionKey) {
        this.libraryUserRepository = libraryUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authSessionKey = authSessionKey;
    }

    @Transactional(readOnly = true)
    public LoginResponseDTO login(LoginRequestDTO request, HttpSession session) {
        LibraryUser user = libraryUserRepository.findByEmail(request.email())
                .orElseThrow(() -> new UnauthorizedException("Credenciais invalidas"));

        if (!user.isActive()) {
            throw new UnauthorizedException("Usuario inativo");
        }

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new UnauthorizedException("Credenciais invalidas");
        }

        AuthUser authUser = new AuthUser(user.getId(), user.getName(), user.getEmail(), user.getRole());
        session.setAttribute(authSessionKey, authUser);

        return new LoginResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getRole(), "Login realizado com sucesso");
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }

    public AuthUser getCurrentUser(HttpSession session) {
        AuthUser user = (AuthUser) session.getAttribute(authSessionKey);
        if (user == null) {
            throw new UnauthorizedException("Usuario nao autenticado");
        }
        return user;
    }
}
