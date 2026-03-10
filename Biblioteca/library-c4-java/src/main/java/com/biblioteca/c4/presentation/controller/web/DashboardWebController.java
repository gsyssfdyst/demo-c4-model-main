package com.biblioteca.c4.presentation.controller.web;

import com.biblioteca.c4.application.service.DashboardService;
import com.biblioteca.c4.common.security.Authenticated;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Authenticated
public class DashboardWebController {

    private final DashboardService dashboardService;

    public DashboardWebController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("title", "Dashboard");
        model.addAttribute("dashboard", dashboardService.getData());
        return "dashboard";
    }
}
