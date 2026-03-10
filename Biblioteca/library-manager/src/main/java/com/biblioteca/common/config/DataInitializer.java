package com.biblioteca.common.config;

import com.biblioteca.domain.model.Book;
import com.biblioteca.domain.model.LibraryUser;
import com.biblioteca.domain.model.enums.UserRole;
import com.biblioteca.persistence.repository.BookRepository;
import com.biblioteca.persistence.repository.LibraryUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initializeData(BookRepository bookRepository, 
                                           LibraryUserRepository userRepository) {
        return args -> {
            // Create sample books
            Book book1 = new Book(
                "Clean Code",
                "Robert C. Martin",
                "978-0132350884",
                LocalDate.of(2008, 8, 1)
            );
            
            Book book2 = new Book(
                "Design Patterns",
                "Gang of Four",
                "978-0201633610",
                LocalDate.of(1994, 10, 31)
            );
            
            Book book3 = new Book(
                "Refactoring",
                "Martin Fowler",
                "978-0201485677",
                LocalDate.of(1999, 7, 8)
            );
            
            bookRepository.save(book1);
            bookRepository.save(book2);
            bookRepository.save(book3);
            
            // Create sample users
            LibraryUser user1 = new LibraryUser(
                "João Silva",
                "joao@example.com",
                UserRole.READER
            );
            
            LibraryUser user2 = new LibraryUser(
                "Maria Santos",
                "maria@example.com",
                UserRole.LIBRARIAN
            );
            
            userRepository.save(user1);
            userRepository.save(user2);
            
            System.out.println("=== Database initialized with sample data ===");
            System.out.println("Books: 3");
            System.out.println("Users: 2 (1 Reader, 1 Librarian)");
            System.out.println("H2 Console: http://localhost:8080/h2-console");
            System.out.println("==========================================");
        };
    }
}
