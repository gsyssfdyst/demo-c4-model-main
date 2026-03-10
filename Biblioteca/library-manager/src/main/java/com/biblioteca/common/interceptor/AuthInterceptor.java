package com.biblioteca.common.interceptor;

import com.biblioteca.common.model.AuthUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) 
            throws Exception {
        
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        // Permitir /login e recursos estáticos
        if (requestURI.startsWith("/login") || 
            requestURI.startsWith("/public/") ||
            requestURI.startsWith("/css/") ||
            requestURI.startsWith("/js/") ||
            requestURI.startsWith("/images/")) {
            return true;
        }

        // Permitir /api/** (validação diferente via interceptor de roles)
        if (requestURI.startsWith("/api/")) {
            return true;
        }

        // Para /web/** exigir autenticação
        if (requestURI.startsWith("/web/")) {
            HttpSession session = request.getSession(false);
            
            if (session == null || session.getAttribute("authUser") == null) {
                response.sendRedirect("/login");
                return false;
            }

            AuthUser user = (AuthUser) session.getAttribute("authUser");
            if (user == null) {
                response.sendRedirect("/login");
                return false;
            }

            // Validar permissões de escrita
            if ("POST".equals(method) || "PUT".equals(method) || "DELETE".equals(method)) {
                // Verificar se é LIBRARIAN
                if (!user.isLibrarian()) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.sendRedirect("/web/dashboard?error=forbidden");
                    return false;
                }
            }
        }

        return true;
    }
}
