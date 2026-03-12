package com.biblioteca.controller;

import com.biblioteca.model.User;
import com.biblioteca.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    // Simulate login for this prototype (in production this would generate a JWT token)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        if (email == null || password == null) {
            return ResponseEntity.badRequest().body("Email and password are required.");
        }

        Optional<User> userOpt = userService.getUserByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // In a real application, we would hash and compare using BCrypt
            if (user.getPassword().equals(password)) {
                // Return user without password for simpler frontend simulation
                user.setPassword(null);
                return ResponseEntity.ok(user);
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password.");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> payload) {
        String name = payload.get("name");
        String email = payload.get("email");
        String password = payload.get("password");

        if (name == null || email == null || password == null) {
            return ResponseEntity.badRequest().body("Nome, email e senha são obrigatórios.");
        }

        if (userService.getUserByEmail(email).isPresent()) {
            return ResponseEntity.badRequest().body("E-mail já está em uso.");
        }

        User newUser = new User(name, email, password, com.biblioteca.model.Role.CLIENT);
        userService.saveUser(newUser);

        // Retorna o usuário criado (sem a senha por segurança)
        newUser.setPassword(null);
        return ResponseEntity.ok(newUser);
    }
}
