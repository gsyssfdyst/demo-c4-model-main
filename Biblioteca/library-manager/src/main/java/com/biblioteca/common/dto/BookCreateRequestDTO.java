package com.biblioteca.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class BookCreateRequestDTO {
    
    @NotBlank(message = "Título do livro é obrigatório")
    private String title;

    @NotBlank(message = "Autor é obrigatório")
    private String author;

    @NotBlank(message = "ISBN é obrigatório")
    private String isbn;

    @NotNull(message = "Data de publicação é obrigatória")
    private LocalDate publishedDate;

    public BookCreateRequestDTO() {
    }

    public BookCreateRequestDTO(String title, String author, String isbn, LocalDate publishedDate) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publishedDate = publishedDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }
}
