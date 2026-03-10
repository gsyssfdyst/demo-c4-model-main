package com.biblioteca.c4.application.service;

import com.biblioteca.c4.common.dto.BookCreateRequestDTO;
import com.biblioteca.c4.common.dto.BookDTO;
import com.biblioteca.c4.common.exception.BusinessRuleException;
import com.biblioteca.c4.common.exception.ResourceNotFoundException;
import com.biblioteca.c4.domain.model.Book;
import com.biblioteca.c4.persistence.repository.BookRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional(readOnly = true)
    public List<BookDTO> listAll() {
        return bookRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public BookDTO getById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro nao encontrado: " + id));
        return toDTO(book);
    }

    @Transactional
    public BookDTO create(BookCreateRequestDTO request) {
        bookRepository.findByIsbn(request.isbn()).ifPresent(book -> {
            throw new BusinessRuleException("ISBN ja cadastrado: " + request.isbn());
        });

        Book book = new Book();
        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setIsbn(request.isbn());
        book.setPublishedDate(request.publishedDate());
        book.setAvailable(request.available());

        return toDTO(bookRepository.save(book));
    }

    @Transactional
    public BookDTO update(Long id, BookCreateRequestDTO request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro nao encontrado: " + id));

        bookRepository.findByIsbn(request.isbn())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new BusinessRuleException("ISBN ja cadastrado: " + request.isbn());
                });

        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setIsbn(request.isbn());
        book.setPublishedDate(request.publishedDate());
        book.setAvailable(request.available());

        return toDTO(bookRepository.save(book));
    }

    @Transactional
    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Livro nao encontrado: " + id);
        }
        bookRepository.deleteById(id);
    }

    public BookDTO toDTO(Book book) {
        return new BookDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPublishedDate(),
                book.isAvailable()
        );
    }
}
