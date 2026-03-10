package com.biblioteca.c4.presentation.controller.web;

import com.biblioteca.c4.application.service.LibraryUserService;
import com.biblioteca.c4.common.dto.LibraryUserCreateRequestDTO;
import com.biblioteca.c4.common.security.Authenticated;
import com.biblioteca.c4.common.security.RoleAllowed;
import com.biblioteca.c4.domain.model.enums.UserRole;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Authenticated
public class UsersWebController {

    private final LibraryUserService libraryUserService;

    public UsersWebController(LibraryUserService libraryUserService) {
        this.libraryUserService = libraryUserService;
    }

    @RoleAllowed({UserRole.ADMIN, UserRole.BIBLIOTECARIO})
    @GetMapping("/users")
    public String list(Model model) {
        model.addAttribute("title", "Usuarios");
        model.addAttribute("users", libraryUserService.listAll());
        model.addAttribute("userForm", new UserForm());
        model.addAttribute("roles", UserRole.values());
        return "users/list";
    }

    @RoleAllowed(UserRole.ADMIN)
    @PostMapping("/users")
    public String create(@ModelAttribute("userForm") @Valid UserForm form) {
        libraryUserService.create(form.toRequest());
        return "redirect:/users";
    }

    @RoleAllowed(UserRole.ADMIN)
    @PostMapping("/users/{id}/delete")
    public String delete(@PathVariable Long id) {
        libraryUserService.delete(id);
        return "redirect:/users";
    }

    public static class UserForm {
        private String name;
        private String email;
        private String password;
        private UserRole role = UserRole.LEITOR;
        private boolean active = true;

        public LibraryUserCreateRequestDTO toRequest() {
            return new LibraryUserCreateRequestDTO(name, email, password, role, active);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public UserRole getRole() {
            return role;
        }

        public void setRole(UserRole role) {
            this.role = role;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }
    }
}
