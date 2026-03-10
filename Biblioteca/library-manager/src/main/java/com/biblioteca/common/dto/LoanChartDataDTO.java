package com.biblioteca.common.dto;

public class LoanChartDataDTO {
    private String label; // Data (ex: "02 Mar") ou status
    private Long count;

    public LoanChartDataDTO() {
    }

    public LoanChartDataDTO(String label, Long count) {
        this.label = label;
        this.count = count;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
