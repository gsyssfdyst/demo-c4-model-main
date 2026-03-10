package com.biblioteca.config;

import com.biblioteca.model.Book;
import com.biblioteca.model.Role;
import com.biblioteca.model.User;
import com.biblioteca.repository.BookRepository;
import com.biblioteca.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.Arrays;
import java.util.List;

@Configuration
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Override
    public void run(String... args) throws Exception {
        // Seed Users if empty
        if (userRepository.count() == 0) {
            User admin = new User("Administrador", "admin@biblioteca.com", "admin123", Role.ADMIN);
            User client1 = new User("João Silva", "joao@email.com", "senha123", Role.CLIENT);
            User client2 = new User("Maria Souza", "maria@email.com", "senha123", Role.CLIENT);

            userRepository.saveAll(Arrays.asList(admin, client1, client2));
        }

        // Seed 15 Books if empty
        if (bookRepository.count() == 0) {
            List<Book> initialBooks = Arrays.asList(
                new Book("O Senhor dos Anéis: A Sociedade do Anel", "J.R.R. Tolkien", new Date(1954 - 1900, 6, 29), true),
                new Book("1984", "George Orwell", new Date(1949 - 1900, 5, 8), true),
                new Book("Dom Quixote", "Miguel de Cervantes", new Date(1605 - 1900, 0, 16), true),
                new Book("O Pequeno Príncipe", "Antoine de Saint-Exupéry", new Date(1943 - 1900, 3, 6), true),
                new Book("Cem Anos de Solidão", "Gabriel García Márquez", new Date(1967 - 1900, 4, 30), true),
                new Book("O Alquimista", "Paulo Coelho", new Date(1988 - 1900, 0, 1), true),
                new Book("A revolução dos bichos", "George Orwell", new Date(1945 - 1900, 7, 17), true),
                new Book("Harry Potter e a Pedra Filosofal", "J.K. Rowling", new Date(1997 - 1900, 5, 26), true),
                new Book("O Diário de Anne Frank", "Anne Frank", new Date(1947 - 1900, 5, 25), true),
                new Book("O Hobbit", "J.R.R. Tolkien", new Date(1937 - 1900, 8, 21), true),
                new Book("Os Miseráveis", "Victor Hugo", new Date(1862 - 1900, 3, 3), true),
                new Book("Memórias Póstumas de Brás Cubas", "Machado de Assis", new Date(1881 - 1900, 0, 1), true),
                new Book("Grande Sertão: Veredas", "João Guimarães Rosa", new Date(1956 - 1900, 0, 1), true),
                new Book("A Menina que Roubava Livros", "Markus Zusak", new Date(2005 - 1900, 8, 1), true),
                new Book("Orgulho e Preconceito", "Jane Austen", new Date(1813 - 1900, 0, 28), true)
            );

            bookRepository.saveAll(initialBooks);
        }
    }
}
