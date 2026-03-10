package com.biblioteca.c4.presentation.controller.web;

import com.biblioteca.c4.common.model.AuthUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(basePackages = "com.biblioteca.c4.presentation.controller.web")
public class WebModelAdvice {

    private final String authSessionKey;

    public WebModelAdvice(@Value("${app.auth.session-key:AUTH_USER}") String authSessionKey) {
        this.authSessionKey = authSessionKey;
    }

    @ModelAttribute("authUser")
    public AuthUser authUser(HttpSession session) {
        return (AuthUser) session.getAttribute(authSessionKey);
    }
}
