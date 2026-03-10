package com.biblioteca.common.dto;

import jakarta.validation.constraints.NotNull;

public class ReturnLoanRequestDTO {
    @NotNull(message = "ID do empréstimo é obrigatório")
    private Long loanId;

    public ReturnLoanRequestDTO() {
    }

    public ReturnLoanRequestDTO(Long loanId) {
        this.loanId = loanId;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }
}
