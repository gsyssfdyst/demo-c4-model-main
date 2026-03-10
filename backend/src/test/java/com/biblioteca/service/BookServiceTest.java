package com.biblioteca.service;

import com.biblioteca.model.Book;
import com.biblioteca.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book testBook;

    @BeforeEach
    void setUp() {
        testBook = new Book("Test Driven Development", "Kent Beck", new Date(), true);
        testBook.setId(1L);
    }

    @Test
    void getAllBooks_ShouldReturnListOfBooks() {
        when(bookRepository.findAll()).thenReturn(Arrays.asList(testBook));
        
        List<Book> books = bookService.getAllBooks();
        
        assertFalse(books.isEmpty());
        assertEquals(1, books.size());
        assertEquals("Test Driven Development", books.get(0).getTitle());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void getBookById_WhenBookExists_ShouldReturnBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        
        Optional<Book> foundBook = bookService.getBookById(1L);
        
        assertTrue(foundBook.isPresent());
        assertEquals(1L, foundBook.get().getId());
        assertEquals("Kent Beck", foundBook.get().getAuthor());
    }

    @Test
    void saveBook_ShouldReturnSavedBook() {
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);
        
        Book newBook = new Book("Test Driven Development", "Kent Beck", new Date(), true);
        Book savedBook = bookService.saveBook(newBook);
        
        assertNotNull(savedBook.getId());
        assertEquals("Test Driven Development", savedBook.getTitle());
    }
}
