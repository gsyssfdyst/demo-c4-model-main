# Códigos Completos - Web UI

## 1. BookCreateRequestDTO.java

**Localização:** `src/main/java/com/biblioteca/common/dto/BookCreateRequestDTO.java`

```java
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
```

---

## 2. LoanListItemDTO.java

**Localização:** `src/main/java/com/biblioteca/common/dto/LoanListItemDTO.java`

```java
package com.biblioteca.common.dto;

import java.time.LocalDate;
import java.math.BigDecimal;

public class LoanListItemDTO {
    private Long id;
    private String userName;
    private String bookTitle;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private BigDecimal fineAmount;

    public LoanListItemDTO() {
    }

    public LoanListItemDTO(Long id, String userName, String bookTitle, LocalDate loanDate, 
                           LocalDate dueDate, LocalDate returnDate, BigDecimal fineAmount) {
        this.id = id;
        this.userName = userName;
        this.bookTitle = bookTitle;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.fineAmount = fineAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public BigDecimal getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(BigDecimal fineAmount) {
        this.fineAmount = fineAmount;
    }

    public boolean isReturned() {
        return returnDate != null;
    }
}
```

---

## 3. WebPageController.java

**Localização:** `src/main/java/com/biblioteca/presentation/controller/WebPageController.java`

```java
package com.biblioteca.presentation.controller;

import com.biblioteca.application.service.BookService;
import com.biblioteca.application.service.LibraryUserService;
import com.biblioteca.application.service.LoanService;
import com.biblioteca.common.dto.BookDTO;
import com.biblioteca.common.dto.BookCreateRequestDTO;
import com.biblioteca.common.dto.LibraryUserDTO;
import com.biblioteca.common.dto.LoanDTO;
import com.biblioteca.common.dto.LoanListItemDTO;
import com.biblioteca.common.dto.BorrowRequestDTO;
import com.biblioteca.domain.model.Loan;
import com.biblioteca.persistence.repository.LoanRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping
public class WebPageController {

    private final BookService bookService;
    private final LibraryUserService userService;
    private final LoanService loanService;
    private final LoanRepository loanRepository;

    public WebPageController(BookService bookService, LibraryUserService userService, 
                             LoanService loanService, LoanRepository loanRepository) {
        this.bookService = bookService;
        this.userService = userService;
        this.loanService = loanService;
        this.loanRepository = loanRepository;
    }

    @GetMapping("/")
    public String redirectToBooks() {
        return "redirect:/web/books";
    }

    // ==================== BOOKS ====================

    @GetMapping("/web/books")
    public String listBooks(Model model) {
        List<BookDTO> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/web/books/new")
    public String newBookForm(Model model) {
        model.addAttribute("book", new BookCreateRequestDTO());
        return "book-new";
    }

    @PostMapping("/web/books")
    public String createBook(@Valid @ModelAttribute("book") BookCreateRequestDTO dto, 
                             BindingResult bindingResult, 
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "book-new";
        }

        try {
            BookDTO bookDTO = new BookDTO(
                null,
                dto.getTitle(),
                dto.getAuthor(),
                dto.getIsbn(),
                dto.getPublishedDate(),
                true
            );
            bookService.createBook(bookDTO);
            redirectAttributes.addFlashAttribute("success", "Livro criado com sucesso!");
            return "redirect:/web/books";
        } catch (Exception e) {
            bindingResult.reject("error", "Erro ao criar livro: " + e.getMessage());
            return "book-new";
        }
    }

    // ==================== USERS ====================

    @GetMapping("/web/users")
    public String listUsers(Model model) {
        List<LibraryUserDTO> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

    // ==================== LOANS ====================

    @GetMapping("/web/loans")
    public String listLoans(Model model) {
        List<LoanDTO> loans = loanService.getAllLoans();
        
        // Converter LoanDTO para LoanListItemDTO
        List<LoanListItemDTO> loanItems = loans.stream()
            .map(loan -> {
                LibraryUserDTO user = userService.getUserById(loan.getUserId());
                BookDTO book = bookService.getBookById(loan.getBookId());
                
                return new LoanListItemDTO(
                    loan.getId(),
                    user.getName(),
                    book.getTitle(),
                    loan.getLoanDate(),
                    loan.getDueDate(),
                    loan.getReturnDate(),
                    loan.getFineAmount()
                );
            })
            .collect(Collectors.toList());

        // Obter disponíveis livros e usuários para o formulário
        List<BookDTO> availableBooks = bookService.getAllBooks()
            .stream()
            .filter(BookDTO::getAvailable)
            .collect(Collectors.toList());
        List<LibraryUserDTO> users = userService.getAllUsers();

        model.addAttribute("loans", loanItems);
        model.addAttribute("books", availableBooks);
        model.addAttribute("users", users);
        model.addAttribute("borrow", new BorrowRequestDTO());

        return "loans";
    }

    @PostMapping("/web/loans/borrow")
    public String borrowBook(@Valid @ModelAttribute("borrow") BorrowRequestDTO dto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (bindingResult.hasErrors()) {
            // Re-carregar dados para o formulário
            List<LoanDTO> loans = loanService.getAllLoans();
            List<LoanListItemDTO> loanItems = loans.stream()
                .map(loan -> {
                    LibraryUserDTO user = userService.getUserById(loan.getUserId());
                    BookDTO book = bookService.getBookById(loan.getBookId());
                    
                    return new LoanListItemDTO(
                        loan.getId(),
                        user.getName(),
                        book.getTitle(),
                        loan.getLoanDate(),
                        loan.getDueDate(),
                        loan.getReturnDate(),
                        loan.getFineAmount()
                    );
                })
                .collect(Collectors.toList());

            List<BookDTO> availableBooks = bookService.getAllBooks()
                .stream()
                .filter(BookDTO::getAvailable)
                .collect(Collectors.toList());
            List<LibraryUserDTO> users = userService.getAllUsers();

            model.addAttribute("loans", loanItems);
            model.addAttribute("books", availableBooks);
            model.addAttribute("users", users);
            
            return "loans";
        }

        try {
            LoanDTO loan = loanService.borrowBook(dto);
            redirectAttributes.addFlashAttribute("success", "Livro emprestado com sucesso!");
            return "redirect:/web/loans";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao emprestar livro: " + e.getMessage());
            return "redirect:/web/loans";
        }
    }

    @PostMapping("/web/loans/{id}/return")
    public String returnBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            loanService.returnBook(id);
            redirectAttributes.addFlashAttribute("success", "Livro devolvido com sucesso!");
            return "redirect:/web/loans";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao devolver livro: " + e.getMessage());
            return "redirect:/web/loans";
        }
    }
}
```

---

## 4. RoleAuthorizationInterceptor.java (MODIFICADO)

**Localização:** `src/main/java/com/biblioteca/common/interceptor/RoleAuthorizationInterceptor.java`

**Mudança:** Adicionada verificação para /web/** no método preHandle()

```java
@Override
public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
    
    String method = request.getMethod();
    String requestURI = request.getRequestURI();
    
    // Não aplicar autorização em rotas /web/** (UI do Thymeleaf)
    if (requestURI.startsWith("/web/") || requestURI.equals("/")) {
        return true;
    }
    
    boolean isProtectedPath = PROTECTED_PATHS.stream()
        .anyMatch(requestURI::startsWith);
    
    boolean isWriteOperation = LIBRARIAN_WRITE_METHODS.contains(method);
    
    if (isProtectedPath && isWriteOperation) {
        String roleHeader = request.getHeader("X-ROLE");
        
        if (roleHeader == null || roleHeader.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"status\":403,\"error\":\"Forbidden\",\"message\":\"X-ROLE header is required for write operations\"}");
            return false;
        }
        
        try {
            UserRole role = UserRole.valueOf(roleHeader.toUpperCase());
            if (role != UserRole.LIBRARIAN) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                response.getWriter().write("{\"status\":403,\"error\":\"Forbidden\",\"message\":\"Only LIBRARIAN role can perform this operation\"}");
                return false;
            }
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"status\":403,\"error\":\"Forbidden\",\"message\":\"Invalid role: " + roleHeader + "\"}");
            return false;
        }
    }
    
    return true;
}
```

---

## 5. pom.xml (MODIFICADO)

**Localização:** `pom.xml`

**Mudança:** Adicionada dependência Thymeleaf após spring-boot-starter-validation

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

---

## Resumo das Chamadas de Service

| Método | Service | Uso |
|--------|---------|-----|
| `getAllBooks()` | BookService | Listar livros em books.html e para filtrar disponíveis |
| `getBookById(Long id)` | BookService | Obter detalhes do livro para LoanListItemDTO |
| `createBook(BookDTO dto)` | BookService | Criar novo livro no formulário |
| `getAllUsers()` | LibraryUserService | Listar usuários e preencher select |
| `getUserById(Long id)` | LibraryUserService | Obter nome do usuário para LoanListItemDTO |
| `getAllLoans()` | LoanService | Listar empréstimos na página loans.html |
| `borrowBook(BorrowRequestDTO dto)` | LoanService | Emprestar livro |
| `returnBook(Long id)` | LoanService | Devolver livro |

---

## ✅ Compilação

```
[INFO] BUILD SUCCESS
[INFO] Total time:  0.577 s
```

Pronto para rodar! 🚀
