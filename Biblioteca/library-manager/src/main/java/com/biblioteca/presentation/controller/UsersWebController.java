package com.biblioteca.presentation.controller;

import com.biblioteca.application.service.LibraryUserService;
import com.biblioteca.common.dto.LibraryUserDTO;
import com.biblioteca.common.model.AuthUser;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/web/users")
public class UsersWebController {

    private final LibraryUserService userService;

    public UsersWebController(LibraryUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String listUsers(Model model, HttpSession session) {
        AuthUser authUser = (AuthUser) session.getAttribute("authUser");
        List<LibraryUserDTO> users = userService.getAllUsers();
        
        model.addAttribute("users", users);
        model.addAttribute("authUser", authUser);
        model.addAttribute("page", "users");
        
        return "users/list";
    }

    @GetMapping("/{id}/edit")
    public String editUser(@PathVariable Long id, Model model, HttpSession session) {
        AuthUser authUser = (AuthUser) session.getAttribute("authUser");
        
        if (!authUser.isLibrarian()) {
            return "redirect:/web/users?error=forbidden";
        }
        
        LibraryUserDTO user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("authUser", authUser);
        model.addAttribute("page", "users");
        
        return "users/edit";
    }
}
