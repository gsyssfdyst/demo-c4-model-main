package com.biblioteca.c4.persistence.repository;

import com.biblioteca.c4.domain.model.LibraryUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryUserRepository extends JpaRepository<LibraryUser, Long> {
    Optional<LibraryUser> findByEmail(String email);

    long countByActiveTrue();
}
