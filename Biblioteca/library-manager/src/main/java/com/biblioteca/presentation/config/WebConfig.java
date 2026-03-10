package com.biblioteca.presentation.config;

import com.biblioteca.common.interceptor.AuthInterceptor;
import com.biblioteca.common.interceptor.RoleAuthorizationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Interceptor de autenticação (deve vir primeiro)
        registry.addInterceptor(new AuthInterceptor())
            .addPathPatterns("/**")
            .excludePathPatterns("/login", "/public/**", "/css/**", "/js/**", "/images/**");

        // Interceptor de autorização por role (para /api/**)
        registry.addInterceptor(new RoleAuthorizationInterceptor())
            .addPathPatterns("/api/**");
    }
}
