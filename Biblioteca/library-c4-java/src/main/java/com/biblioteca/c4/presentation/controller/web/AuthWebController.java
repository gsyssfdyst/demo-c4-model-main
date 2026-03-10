package com.biblioteca.c4.presentation.controller.web;

import com.biblioteca.c4.application.service.AuthService;
import com.biblioteca.c4.common.dto.LoginRequestDTO;
import com.biblioteca.c4.common.exception.UnauthorizedException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthWebController {

    private final AuthService authService;

    public AuthWebController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("title", "Login");
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam @Email String email,
                        @RequestParam @NotBlank String password,
                        HttpSession session,
                        Model model) {
        try {
            authService.login(new LoginRequestDTO(email, password), session);
            return "redirect:/dashboard";
        } catch (UnauthorizedException ex) {
            model.addAttribute("title", "Login");
            model.addAttribute("error", ex.getMessage());
            return "login";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        authService.logout(session);
        return "redirect:/login";
    }
}
