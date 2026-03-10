# Arquitetura - Sistema de Gestão de Biblioteca

## Visão Geral

Este projeto implementa um Sistema de Gestão de Biblioteca (MVP) com uma arquitetura **Monólito Modular em Camadas** que segue o padrão de separação de responsabilidades em 5 camadas principais:

```
Presentation Layer (Controllers)
         ↓
Application Layer (Services)
         ↓
Domain Layer (Models)
         ↓
Persistence Layer (Repositories)
         ↓
Integration Layer (Adapters)
```

## As 5 Camadas

### 1. **Presentation Layer** (`presentation.controller`)

**Responsabilidade:** Expor a API REST e gerenciar requisições/respostas HTTP.

**Componentes:**
- `BookController` - CRUD de livros
- `LibraryUserController` - CRUD de usuários
- `LoanController` - Empréstimos e devoluções

**Características:**
- Controllers recebem DTOs e mapeiam entre HTTP e aplicação
- Não acessam Repositories diretamente
- Delegam toda lógica aos Services
- Validação via @Valid

**Exemplo:**
```java
@PostMapping
public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO dto) {
    BookDTO created = bookService.createBook(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
}
```

### 2. **Application Layer** (`application.service`)

**Responsabilidade:** Orquestrar a lógica de negócio (casos de uso).

**Componentes:**
- `BookService` - Operações com livros
- `LibraryUserService` - Operações com usuários
- `LoanService` - Empréstimos, devoluções e cálculo de multas

**Características:**
- Coordena múltiplos Repositories
- Gerencia transações via @Transactional
- Implementa regras de negócio (ex: cálculo de multa)
- Chama Adapters de integração
- Responsável por orquestração

**Exemplo de Regra de Negócio:**
```java
public LoanDTO returnBook(Long loanId) {
    Loan loan = loanRepository.findById(loanId)
        .orElseThrow(() -> new ResourceNotFoundException(...));
    
    LocalDate returnDate = LocalDate.now();
    loan.setReturnDate(returnDate);
    
    if (returnDate.isAfter(loan.getDueDate())) {
        long daysLate = ChronoUnit.DAYS.between(loan.getDueDate(), returnDate);
        BigDecimal fineAmount = FINE_PER_DAY.multiply(new BigDecimal(daysLate));
        loan.setFineAmount(fineAmount);
        
        // Chamada ao Adapter de Integração
        notificationService.sendFineNotification(loan.getUser().getId(), loanId, fineAmount);
    }
    
    // Atualizar disponibilidade do livro
    Book book = loan.getBook();
    book.setAvailable(true);
    bookRepository.save(book);
    
    return convertToDTO(loanRepository.save(loan));
}
```

### 3. **Domain Layer** (`domain.model`)

**Responsabilidade:** Definir as entidades e regras de domínio.

**Componentes:**
- `Book` - Entidade de livro
- `LibraryUser` - Entidade de usuário
- `Loan` - Entidade de empréstimo
- `UserRole` (enum) - Papéis de usuário

**Características:**
- Entidades JPA puras (sem Lombok)
- Getters/Setters explícitos
- Anotações JPA: @Entity, @Table, @ManyToOne, @GeneratedValue
- Validações via Bean Validation: @NotBlank, @Email
- Zero dependências de frameworks

**Relacionamentos:**
```
Loan (ManyToOne) -> LibraryUser
Loan (ManyToOne) -> Book
```

### 4. **Persistence Layer** (`persistence.repository`)

**Responsabilidade:** Abstração de acesso a dados (JPA).

**Componentes:**
- `BookRepository` - Operações de persistência de Book
- `LibraryUserRepository` - Operações de persistência de LibraryUser
- `LoanRepository` - Operações de persistência de Loan

**Características:**
- Interfaces que estendem JpaRepository
- Spring gera implementações automaticamente
- CRUD basic e queries customizadas
- Zero implementação manual (gerada pelo Spring)

**Exemplo:**
```java
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbn(String isbn);
}
```

### 5. **Integration Layer** (`integration.notification`)

**Responsabilidade:** Conectar a aplicação com sistemas externos (adapters).

**Componentes:**
- `NotificationService` - Adapter para enviar notificações

**Características:**
- Implementação atual: logs ao console (SLF4J)
- Fácil substituir por SMS, Email, Message Queue, etc.
- Não conhece Controllers
- Chamado apenas por Services

**Exemplo:**
```java
@Service
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    
    public boolean sendFineNotification(Long userId, Long loanId, BigDecimal fineAmount) {
        logger.info("UserId: {}", userId);
        logger.info("LoanId: {}", loanId);
        logger.info("FineAmount: R$ {}", fineAmount);
        return true;
    }
}
```

## Camadas Transversais (Common)

Código compartilhado entre as 5 camadas principais.

### 1. **Exceções** (`common.exception`)
- `ResourceNotFoundException` - Recurso não encontrado (404)
- `BookNotAvailableException` - Livro indisponível (400)

### 2. **DTOs** (`common.dto`)
- `BookDTO` - Transfer Object para Book
- `LibraryUserDTO` - Transfer Object para LibraryUser
- `LoanDTO` - Transfer Object para Loan
- `BorrowRequestDTO` - Request DTO para emprestar
- `ReturnLoanRequestDTO` - Request DTO para devolver
- `ErrorResponseDTO` - Response DTO para erros

**Por que DTOs?**
- Não expor entidades JPA diretamente na API
- Desacoplar estrutura interna de externos
- Versioning da API sem quebrar a lógica

### 3. **Handlers de Exceção** (`common.handler`)
- `GlobalExceptionHandler` - @ControllerAdvice para tratamento centralizado

**Características:**
- Intercepta todas exceções de @RestControllers
- Retorna JSON padronizado: timestamp, status, error, message, path
- Específico para cada tipo de erro

**Exemplo de Resposta:**
```json
{
  "timestamp": "2026-03-02T15:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Book not found with id: 999",
  "path": "/api/books/999"
}
```

### 4. **Interceptadores** (`common.interceptor`)
- `RoleAuthorizationInterceptor` - Validação de autorização por role

**Fluxo de Autenticação:**
1. Interceptor intercepta requisição ANTES de chegar ao Controller
2. Verifica se é operação de escrita (POST, PUT, DELETE)
3. Se sim, valida header `X-ROLE`
4. Se role ≠ LIBRARIAN, retorna 403
5. Se válido, passa para o Controller

### 5. **Configurações** (`common.config`)
- `WebConfig` - Registra Interceptors
- `DataInitializer` - Seed de dados (CommandLineRunner)

---

## Fluxo de uma Requisição

### Exemplo: POST /api/books (criar livro)

```
1. Requisição HTTP chega com header X-ROLE: LIBRARIAN
   ↓
2. RoleAuthorizationInterceptor.preHandle()
   - Valida se é operação de escrita (POST ✓)
   - Valida X-ROLE header
   - Se LIBRARIAN ✓ → continua
   ↓
3. BookController.createBook(@Valid @RequestBody BookDTO dto)
   - Recebe DTO
   - @Valid valida constraints (@NotBlank, etc)
   - Chama bookService.createBook(dto)
   ↓
4. BookService.createBook(BookDTO dto)
   - Converte DTO → Entity
   - Cria nova entidade Book
   - Chama bookRepository.save(book)
   ↓
5. BookRepository.save(Book)
   - Spring gera SQL INSERT
   - H2 persiste no banco
   - Retorna Book com ID gerado
   ↓
6. BookService converte Entity → DTO
   ↓
7. BookController retorna ResponseEntity<BookDTO> com status 201
   ↓
8. JSON Response retorna ao cliente
```

### Exemplo: POST /api/loans/{id}/return (devolver com multa)

```
1. Requisição HTTP chega
   ↓
2. Interceptor valida (GET → passa sem X-ROLE)
   ↓
3. LoanController.returnBook(@PathVariable Long id)
   - Chama loanService.returnBook(id)
   ↓
4. LoanService.returnBook(Long loanId)
   - LoanRepository.findById() → obtém Loan
   - Calcula returnDate = LocalDate.now()
   - Verifica se returnDate > dueDate (atraso?)
   - Se SIM:
     a) Calcula daysLate
     b) Calcula fineAmount = daysLate * 2.00
     c) Chama notificationService.sendFineNotification()
        ↓
5. NotificationService.sendFineNotification()
   - Escreve LOG: FineNotification
   - Retorna true
   
   - Volta para LoanService
   ↓
6. LoanService marca Book.available = true
   - BookRepository.save(book)
   ↓
7. LoanRepository.save(loan com returnDate e fineAmount)
   ↓
8. Converte para DTO e retorna
   ↓
9. JSON Response com status 200 OK
```

---

## Princípios de Design Implementados

### 1. **Single Responsibility Principle (SRP)**
- Cada classe tem uma responsabilidade bem definida
- BookController: receber HTTP
- BookService: lógica
- BookRepository: persistência

### 2. **Dependency Injection**
- Todas injeções via constructor
- Facilita testes unitários
- Desacoplamento

```java
@Service
public class BookService {
    private final BookRepository bookRepository;
    
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
}
```

### 3. **Layered Architecture (N-Tier)**
- Separação clara de responsabilidades
- Fácil entender fluxo de dados
- Fácil trocar implementações

### 4. **Adapter Pattern**
- NotificationService é um adapter
- Fácil trocar implementação (SMS, Email, MQ)
- Não quebra a aplicação

### 5. **DTO Pattern**
- Desacoplamento entre API e modelo interno
- Possibilita versionamento de API
- Segurança (não expor estrutura interna)

### 6. **Repository Pattern**
- Abstração sobre acesso a dados
- Fácil trocar banco de dados
- Facilita testes com mocks

---

## Fluxo de Dados para Segurança

```
Request com X-ROLE: LIBRARIAN
    ↓
RoleAuthorizationInterceptor
    ↓
    ├─ É GET? → Passa (PUBLIC)
    └─ É POST/PUT/DELETE? → Valida X-ROLE
        ├─ X-ROLE = LIBRARIAN? → Passa
        └─ X-ROLE ≠ LIBRARIAN? → 403 Forbidden
```

---

## Validação de Dados

### Bean Validation no Domain
```java
@Entity
public class Book {
    @NotBlank(message = "Título do livro é obrigatório")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Autor é obrigatório")
    @Column(nullable = false)
    private String author;

    @NotBlank(message = "ISBN é obrigatório")
    @Column(nullable = false, unique = true)
    private String isbn;
}
```

### Uso em Controller
```java
@PostMapping
public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO dto) {
    // @Valid dispara validação automaticamente
}
```

---

## Transações

### Servicios com @Transactional
```java
@Service
@Transactional  // Default é propagation REQUIRED, isolation DEFAULT
public class BookService {
    
    @Transactional(readOnly = true)  // Otimizado para leitura
    public BookDTO getBookById(Long id) { ... }
    
    @Transactional  // Escrita
    public BookDTO createBook(BookDTO dto) { ... }
}
```

**Benefícios:**
- Atomicidade: tudo ou nada
- Consistência: estado válido
- Isolamento entre transações
- Durabilidade: persistido

---

## Tratamento de Erros Centralizado

```java
@ControllerAdvice  // Intercepta todos os @RestControllers
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFound(...) {
        // Retorna JSON padronizado
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(...) {
        // Retorna erros de validação
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(...) {
        // Fallback para inesperados
    }
}
```

---

## Conclusão

A arquitetura implementada oferece:

✅ **Separação de Responsabilidades** - Fácil manutenção e evolução
✅ **Testabilidade** - Cada camada independente
✅ **Escalabilidade** - Fácil adicionar novos features
✅ **Segurança** - Validação em múltiplos níveis
✅ **Resiliência** - Tratamento de erros centralizado
✅ **Adaptabilidade** - Fácil trocar implementações

Ideal para projetos que evoluem, têm múltiplos desenvolvedores e precisam manutenção a longo prazo.
