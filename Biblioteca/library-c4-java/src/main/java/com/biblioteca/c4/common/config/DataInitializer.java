package com.biblioteca.c4.common.config;

import com.biblioteca.c4.domain.model.Book;
import com.biblioteca.c4.domain.model.LibraryUser;
import com.biblioteca.c4.domain.model.enums.UserRole;
import com.biblioteca.c4.persistence.repository.BookRepository;
import com.biblioteca.c4.persistence.repository.LibraryUserRepository;
import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final LibraryUserRepository libraryUserRepository;
    private final BookRepository bookRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(LibraryUserRepository libraryUserRepository,
                           BookRepository bookRepository,
                           PasswordEncoder passwordEncoder) {
        this.libraryUserRepository = libraryUserRepository;
        this.bookRepository = bookRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (libraryUserRepository.count() == 0) {
            createUser("Admin", "admin@biblioteca.local", "admin123", UserRole.ADMIN);
            createUser("Bibliotecario", "biblio@biblioteca.local", "biblio123", UserRole.BIBLIOTECARIO);
            createUser("Leitor", "leitor@biblioteca.local", "leitor123", UserRole.LEITOR);
        }

        if (bookRepository.count() == 0) {
            createBook("Clean Code", "Robert C. Martin", "9780132350884", LocalDate.of(2008, 8, 1));
            createBook("Effective Java", "Joshua Bloch", "9780134685991", LocalDate.of(2018, 1, 6));
            createBook("Domain-Driven Design", "Eric Evans", "9780321125217", LocalDate.of(2003, 8, 30));
        }
    }

    private void createUser(String name, String email, String rawPassword, UserRole role) {
        LibraryUser user = new LibraryUser();
        user.setName(name);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        user.setActive(true);
        libraryUserRepository.save(user);
    }

    private void createBook(String title, String author, String isbn, LocalDate publishedDate) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        book.setPublishedDate(publishedDate);
        book.setAvailable(true);
        bookRepository.save(book);
    }
}
