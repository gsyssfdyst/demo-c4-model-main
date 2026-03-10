package com.biblioteca.application.service;

import com.biblioteca.common.model.AuthUser;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    // In-memory user database for demo
    private static final Map<String, AuthUser> USERS = new HashMap<>();

    static {
        USERS.put("joao", new AuthUser(1L, "joao", "joao@example.com", "READER"));
        USERS.put("maria", new AuthUser(2L, "maria", "maria@example.com", "LIBRARIAN"));
        USERS.put("pedro", new AuthUser(3L, "pedro", "pedro@example.com", "READER"));
        USERS.put("admin", new AuthUser(99L, "admin", "admin@example.com", "LIBRARIAN"));
    }

    /**
     * Autentica usuário (demo: qualquer password 123)
     */
    public AuthUser authenticate(String username, String password) {
        if (username == null || password == null) {
            return null;
        }

        // Demo: aceita qualquer password se usuário existe
        AuthUser user = USERS.get(username);
        if (user != null && "123".equals(password)) {
            return user;
        }

        return null;
    }

    /**
     * Retorna usuário por username
     */
    public AuthUser getUserByUsername(String username) {
        return USERS.get(username);
    }

    /**
     * Retorna todos os usuários de demo (para mock)
     */
    public Map<String, AuthUser> getAllUsers() {
        return new HashMap<>(USERS);
    }
}
