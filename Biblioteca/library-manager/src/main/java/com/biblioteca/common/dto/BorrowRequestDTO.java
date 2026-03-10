package com.biblioteca.common.dto;

import jakarta.validation.constraints.NotNull;

public class BorrowRequestDTO {
    @NotNull(message = "ID do usuário é obrigatório")
    private Long userId;

    @NotNull(message = "ID do livro é obrigatório")
    private Long bookId;

    public BorrowRequestDTO() {
    }

    public BorrowRequestDTO(Long userId, Long bookId) {
        this.userId = userId;
        this.bookId = bookId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
}
