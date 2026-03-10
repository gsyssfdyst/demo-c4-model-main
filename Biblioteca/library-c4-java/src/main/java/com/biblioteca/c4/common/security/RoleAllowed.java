package com.biblioteca.c4.common.security;

import com.biblioteca.c4.domain.model.enums.UserRole;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RoleAllowed {
    UserRole[] value();
}
