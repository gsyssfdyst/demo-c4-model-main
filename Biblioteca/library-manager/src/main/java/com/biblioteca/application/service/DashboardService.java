package com.biblioteca.application.service;

import com.biblioteca.common.dto.*;
import com.biblioteca.domain.model.Loan;
import com.biblioteca.persistence.repository.LoanRepository;
import com.biblioteca.persistence.repository.LibraryUserRepository;
import com.biblioteca.persistence.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final LoanRepository loanRepository;
    private final LibraryUserRepository userRepository;
    private final BookRepository bookRepository;

    public DashboardService(LoanRepository loanRepository, LibraryUserRepository userRepository, 
                            BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    /**
     * Retorna DTO com todos os dados do dashboard
     */
    public DashboardDTO getDashboardData() {
        List<KPICardDTO> kpiCards = buildKPICards();
        List<LoanChartDataDTO> chartData = buildChartData();
        List<RecentLoanDTO> recentLoans = buildRecentLoans();

        return new DashboardDTO(kpiCards, chartData, recentLoans, "bar");
    }

    /**
     * Constrói cards de KPI
     */
    private List<KPICardDTO> buildKPICards() {
        List<KPICardDTO> kpis = new ArrayList<>();

        // Total Usuários
        Long totalUsers = userRepository.count();
        kpis.add(new KPICardDTO(
            "users",
            "Total Usuários",
            totalUsers.toString(),
            "👥",
            "primary"
        ));

        // Total Livros
        Long totalBooks = bookRepository.count();
        kpis.add(new KPICardDTO(
            "books",
            "Total Livros",
            totalBooks.toString(),
            "📚",
            "info"
        ));

        // Empréstimos Ativos
        long activeLoans = loanRepository.findAll().stream()
            .filter(loan -> loan.getReturnDate() == null)
            .count();
        kpis.add(new KPICardDTO(
            "active",
            "Empréstimos Ativos",
            String.valueOf(activeLoans),
            "🔄",
            "success"
        ));

        // Empréstimos Atrasados
        long overdueLoans = loanRepository.findAll().stream()
            .filter(loan -> loan.getReturnDate() == null && 
                          loan.getDueDate().isBefore(LocalDate.now()))
            .count();
        kpis.add(new KPICardDTO(
            "overdue",
            "Empréstimos Atrasados",
            String.valueOf(overdueLoans),
            "⏰",
            "warning"
        ));

        // Multa Total
        BigDecimal totalFine = loanRepository.findAll().stream()
            .map(Loan::getFineAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        kpis.add(new KPICardDTO(
            "fines",
            "Multa Total",
            String.format("R$ %.2f", totalFine),
            "💰",
            "danger"
        ));

        return kpis;
    }

    /**
     * Constrói dados para gráfico (últimos 7 dias)
     */
    private List<LoanChartDataDTO> buildChartData() {
        List<LoanChartDataDTO> chartData = new ArrayList<>();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM");

        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            String label = date.format(formatter);
            
            // Conta empréstimos iniciados neste dia
            long count = loanRepository.findAll().stream()
                .filter(loan -> loan.getLoanDate().equals(date))
                .count();

            chartData.add(new LoanChartDataDTO(label, count));
        }

        return chartData;
    }

    /**
     * Retorna últimos 10 empréstimos (top recentes)
     */
    private List<RecentLoanDTO> buildRecentLoans() {
        List<RecentLoanDTO> recentLoans = new ArrayList<>();

        loanRepository.findAll().stream()
            .sorted(Comparator.comparing(Loan::getId).reversed())
            .limit(10)
            .forEach(loan -> {
                String userName = loan.getUser().getName();
                String bookTitle = loan.getBook().getTitle();
                
                String statusBadge;
                if (loan.getReturnDate() != null) {
                    statusBadge = "Devolvido";
                } else if (loan.getDueDate().isBefore(LocalDate.now())) {
                    statusBadge = "Atrasado";
                } else {
                    statusBadge = "Ativo";
                }

                recentLoans.add(new RecentLoanDTO(
                    loan.getId(),
                    userName,
                    bookTitle,
                    loan.getLoanDate(),
                    loan.getDueDate(),
                    loan.getReturnDate(),
                    statusBadge
                ));
            });

        return recentLoans;
    }
}
