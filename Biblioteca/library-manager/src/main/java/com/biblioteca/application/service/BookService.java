package com.biblioteca.application.service;

import com.biblioteca.common.dto.BookDTO;
import com.biblioteca.common.exception.ResourceNotFoundException;
import com.biblioteca.domain.model.Book;
import com.biblioteca.persistence.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public BookDTO createBook(BookDTO dto) {
        Book book = new Book(
            dto.getTitle(),
            dto.getAuthor(),
            dto.getIsbn(),
            dto.getPublishedDate()
        );
        Book savedBook = bookRepository.save(book);
        return convertToDTO(savedBook);
    }

    @Transactional(readOnly = true)
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        return convertToDTO(book);
    }

    public BookDTO updateBook(Long id, BookDTO dto) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setIsbn(dto.getIsbn());
        book.setPublishedDate(dto.getPublishedDate());
        book.setAvailable(dto.getAvailable());
        
        Book updatedBook = bookRepository.save(book);
        return convertToDTO(updatedBook);
    }

    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        bookRepository.delete(book);
    }

    private BookDTO convertToDTO(Book book) {
        return new BookDTO(
            book.getId(),
            book.getTitle(),
            book.getAuthor(),
            book.getIsbn(),
            book.getPublishedDate(),
            book.getAvailable()
        );
    }
}
