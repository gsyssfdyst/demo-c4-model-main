package com.biblioteca.presentation.controller;

import com.biblioteca.application.service.AuthService;
import com.biblioteca.common.dto.LoginRequestDTO;
import com.biblioteca.common.model.AuthUser;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public String showLoginForm(Model model) {
        model.addAttribute("loginRequest", new LoginRequestDTO());
        return "login";
    }

    @PostMapping
    public String handleLogin(@Valid @ModelAttribute("loginRequest") LoginRequestDTO dto,
                              BindingResult bindingResult,
                              HttpSession session,
                              Model model) {
        if (bindingResult.hasErrors()) {
            return "login";
        }

        AuthUser user = authService.authenticate(dto.getUsername(), dto.getPassword());
        
        if (user == null) {
            model.addAttribute("error", "Usuário ou senha inválidos");
            return "login";
        }

        // Salvar na sessão
        session.setAttribute("authUser", user);
        
        return "redirect:/web/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
