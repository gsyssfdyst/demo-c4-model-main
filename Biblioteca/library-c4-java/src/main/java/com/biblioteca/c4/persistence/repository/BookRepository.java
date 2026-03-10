package com.biblioteca.c4.persistence.repository;

import com.biblioteca.c4.domain.model.Book;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbn(String isbn);

    long countByAvailableTrue();
}
