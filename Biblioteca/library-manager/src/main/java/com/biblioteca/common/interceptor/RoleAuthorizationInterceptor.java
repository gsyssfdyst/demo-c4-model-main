package com.biblioteca.common.interceptor;

import com.biblioteca.domain.model.enums.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RoleAuthorizationInterceptor implements HandlerInterceptor {

    private static final Set<String> LIBRARIAN_WRITE_METHODS = new HashSet<>(
        Arrays.asList("POST", "PUT", "DELETE")
    );

    private static final Set<String> PROTECTED_PATHS = new HashSet<>(
        Arrays.asList("/api/books")
    );

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        
        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        
        // Não aplicar autorização em rotas /web/** (UI do Thymeleaf)
        if (requestURI.startsWith("/web/") || requestURI.equals("/")) {
            return true;
        }
        
        boolean isProtectedPath = PROTECTED_PATHS.stream()
            .anyMatch(requestURI::startsWith);
        
        boolean isWriteOperation = LIBRARIAN_WRITE_METHODS.contains(method);
        
        if (isProtectedPath && isWriteOperation) {
            String roleHeader = request.getHeader("X-ROLE");
            
            if (roleHeader == null || roleHeader.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                response.getWriter().write("{\"status\":403,\"error\":\"Forbidden\",\"message\":\"X-ROLE header is required for write operations\"}");
                return false;
            }
            
            try {
                UserRole role = UserRole.valueOf(roleHeader.toUpperCase());
                if (role != UserRole.LIBRARIAN) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"status\":403,\"error\":\"Forbidden\",\"message\":\"Only LIBRARIAN role can perform this operation\"}");
                    return false;
                }
            } catch (IllegalArgumentException e) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                response.getWriter().write("{\"status\":403,\"error\":\"Forbidden\",\"message\":\"Invalid role: " + roleHeader + "\"}");
                return false;
            }
        }
        
        return true;
    }
}
