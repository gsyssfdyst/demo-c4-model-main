package com.biblioteca.c4.common.security;

import com.biblioteca.c4.common.dto.ErrorResponseDTO;
import com.biblioteca.c4.common.model.AuthUser;
import com.biblioteca.c4.domain.model.enums.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SessionAuthInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;
    private final String authSessionKey;

    public SessionAuthInterceptor(ObjectMapper objectMapper,
                                  @Value("${app.auth.session-key:AUTH_USER}") String authSessionKey) {
        this.objectMapper = objectMapper;
        this.authSessionKey = authSessionKey;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod method)) {
            return true;
        }

        RoleAllowed roleAllowed = resolveRoleAllowed(method);
        boolean authenticatedRequired = isAuthenticatedRequired(method) || roleAllowed != null;
        if (!authenticatedRequired) {
            return true;
        }

        HttpSession session = request.getSession(false);
        AuthUser authUser = session != null ? (AuthUser) session.getAttribute(authSessionKey) : null;
        if (authUser == null) {
            return handleUnauthorized(request, response, "Usuario nao autenticado");
        }

        if (roleAllowed != null) {
            Optional<UserRole> matchedRole = Arrays.stream(roleAllowed.value())
                    .filter(role -> role == authUser.role())
                    .findFirst();
            if (matchedRole.isEmpty()) {
                return handleForbidden(request, response, "Usuario sem permissao para esta operacao");
            }
        }

        return true;
    }

    private boolean isAuthenticatedRequired(HandlerMethod method) {
        return method.hasMethodAnnotation(Authenticated.class)
                || method.getBeanType().isAnnotationPresent(Authenticated.class);
    }

    private RoleAllowed resolveRoleAllowed(HandlerMethod method) {
        RoleAllowed methodRole = method.getMethodAnnotation(RoleAllowed.class);
        if (methodRole != null) {
            return methodRole;
        }
        return method.getBeanType().getAnnotation(RoleAllowed.class);
    }

    private boolean handleUnauthorized(HttpServletRequest request,
                                       HttpServletResponse response,
                                       String message) throws Exception {
        if (isApi(request)) {
            writeJsonError(response, HttpStatus.UNAUTHORIZED, message, request.getRequestURI());
        } else {
            response.sendRedirect("/login");
        }
        return false;
    }

    private boolean handleForbidden(HttpServletRequest request,
                                    HttpServletResponse response,
                                    String message) throws Exception {
        if (isApi(request)) {
            writeJsonError(response, HttpStatus.FORBIDDEN, message, request.getRequestURI());
        } else {
            response.sendError(HttpStatus.FORBIDDEN.value(), message);
        }
        return false;
    }

    private boolean isApi(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/api");
    }

    private void writeJsonError(HttpServletResponse response,
                                HttpStatus status,
                                String message,
                                String path) throws Exception {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ErrorResponseDTO payload = new ErrorResponseDTO(
                OffsetDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path
        );
        response.getWriter().write(objectMapper.writeValueAsString(payload));
    }
}
