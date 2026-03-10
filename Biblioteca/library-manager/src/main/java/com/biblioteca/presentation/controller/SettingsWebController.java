package com.biblioteca.presentation.controller;

import com.biblioteca.common.model.AuthUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web/settings")
public class SettingsWebController {

    @GetMapping
    public String showSettings(Model model, HttpSession session) {
        AuthUser authUser = (AuthUser) session.getAttribute("authUser");
        
        model.addAttribute("authUser", authUser);
        model.addAttribute("page", "settings");
        
        return "settings/index";
    }
}
