package com.biblioteca.presentation.controller;

import com.biblioteca.application.service.DashboardService;
import com.biblioteca.common.dto.DashboardDTO;
import com.biblioteca.common.model.AuthUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public String showDashboard(Model model, HttpSession session) {
        AuthUser authUser = (AuthUser) session.getAttribute("authUser");
        
        DashboardDTO dashboard = dashboardService.getDashboardData();
        
        model.addAttribute("dashboard", dashboard);
        model.addAttribute("authUser", authUser);
        
        return "dashboard";
    }
}
