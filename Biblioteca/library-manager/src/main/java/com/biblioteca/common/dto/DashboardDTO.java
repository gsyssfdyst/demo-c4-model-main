package com.biblioteca.common.dto;

import java.util.List;
import java.util.Map;

public class DashboardDTO {
    private List<KPICardDTO> kpiCards;
    private List<LoanChartDataDTO> chartData;
    private List<RecentLoanDTO> recentLoans;
    private String chartType; // bar, line, doughnut

    public DashboardDTO() {
    }

    public DashboardDTO(List<KPICardDTO> kpiCards, List<LoanChartDataDTO> chartData, 
                        List<RecentLoanDTO> recentLoans, String chartType) {
        this.kpiCards = kpiCards;
        this.chartData = chartData;
        this.recentLoans = recentLoans;
        this.chartType = chartType;
    }

    public List<KPICardDTO> getKpiCards() {
        return kpiCards;
    }

    public void setKpiCards(List<KPICardDTO> kpiCards) {
        this.kpiCards = kpiCards;
    }

    public List<LoanChartDataDTO> getChartData() {
        return chartData;
    }

    public void setChartData(List<LoanChartDataDTO> chartData) {
        this.chartData = chartData;
    }

    public List<RecentLoanDTO> getRecentLoans() {
        return recentLoans;
    }

    public void setRecentLoans(List<RecentLoanDTO> recentLoans) {
        this.recentLoans = recentLoans;
    }

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }
}
