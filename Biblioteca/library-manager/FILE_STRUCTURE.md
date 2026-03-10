# 🗂️ Estrutura Final do Projeto

## Árvore Completa de Arquivos

```
library-manager/
│
├── pom.xml                                           # Maven - dependências e build
├── .gitignore                                        # Git ignore patterns
│
├── README.md                                         # Documentação de uso completa + exemplos curl
├── ARCHITECTURE.md                                   # Documentação detalhada da arquitetura
├── SUMMARY.md                                        # Sumário do projeto entregue
│
├── src/main/java/com/biblioteca/
│   │
│   ├── LibraryManagementApplication.java             # Classe Main (@SpringBootApplication)
│   │
│   ├── presentation/controller/
│   │   ├── BookController.java                       # REST endpoints: /api/books
│   │   ├── LibraryUserController.java                # REST endpoints: /api/users
│   │   └── LoanController.java                       # REST endpoints: /api/loans
│   │
│   ├── application/service/
│   │   ├── BookService.java                          # Casos de uso de livros
│   │   ├── LibraryUserService.java                   # Casos de uso de usuários
│   │   └── LoanService.java                          # Casos de uso de empréstimos + multas
│   │
│   ├── domain/model/
│   │   ├── Book.java                                 # Entidade JPA: Livro
│   │   ├── LibraryUser.java                          # Entidade JPA: Usuário
│   │   ├── Loan.java                                 # Entidade JPA: Empréstimo
│   │   └── enums/
│   │       └── UserRole.java                         # Enum: READER, LIBRARIAN
│   │
│   ├── persistence/repository/
│   │   ├── BookRepository.java                       # JPA Repository para Book
│   │   ├── LibraryUserRepository.java                # JPA Repository para LibraryUser
│   │   └── LoanRepository.java                       # JPA Repository para Loan
│   │
│   ├── integration/notification/
│   │   └── NotificationService.java                  # Adapter: Notificações (logs)
│   │
│   └── common/
│       ├── exception/
│       │   ├── ResourceNotFoundException.java         # Exceção customizada: Recurso não encontrado
│       │   └── BookNotAvailableException.java        # Exceção customizada: Livro indisponível
│       │
│       ├── handler/
│       │   └── GlobalExceptionHandler.java           # @ControllerAdvice - Tratamento centralizado
│       │
│       ├── interceptor/
│       │   └── RoleAuthorizationInterceptor.java     # Validação de autorização por role
│       │
│       ├── config/
│       │   ├── WebConfig.java                        # Configuração de interceptors
│       │   └── DataInitializer.java                  # CommandLineRunner - seed de dados
│       │
│       └── dto/
│           ├── BookDTO.java                          # Transfer Object: Livro
│           ├── LibraryUserDTO.java                   # Transfer Object: Usuário
│           ├── LoanDTO.java                          # Transfer Object: Empréstimo
│           ├── BorrowRequestDTO.java                 # Request DTO: Emprestar livro
│           ├── ReturnLoanRequestDTO.java             # Request DTO: Devolver livro
│           └── ErrorResponseDTO.java                 # Response DTO: Erros
│
└── src/main/resources/
    ├── application.yml                               # Configuração Spring Boot (H2, Hibernate)
    └── logback-spring.xml                            # Configuração de logs (SLF4J)

target/                                               # Gerado por Maven (ignorado no .gitignore)
```

---

## 📊 Estatísticas

### Código Java
- **27 classes Java**
  - 3 Controllers (REST)
  - 3 Services (Lógica de Negócio)
  - 3 Entities (JPA)
  - 1 Enum
  - 3 Repositories (JPA)
  - 6 DTOs (Transfer Objects)
  - 2 Exceções customizadas
  - 1 GlobalExceptionHandler
  - 1 RoleAuthorizationInterceptor
  - 2 Config classes
  - 1 NotificationService
  - 1 Application Main class

### Configuração
- 1 pom.xml
- 1 application.yml
- 1 logback-spring.xml
- 1 .gitignore

### Documentação
- 1 README.md (com exemplos curl)
- 1 ARCHITECTURE.md (documentação técnica)
- 1 SUMMARY.md (sumário executivo)
- 1 FILE_STRUCTURE.md (este arquivo)

### Total: 38 arquivos

---

## 🎯 Mapeamento de Responsabilidades

### Path da Requisição: `POST /api/books` (com X-ROLE: LIBRARIAN)

```
presentation/controller/BookController.java
    ↓ (recebe HTTP POST)
application/service/BookService.java
    ↓ (lógica de negócio)
persistence/repository/BookRepository.java
    ↓ (acessa banco)
domain/model/Book.java
    ↓ (persiste no H2)
    ↓ (retorna)
common/dto/BookDTO.java
    ↓ (converte para JSON)
common/handler/GlobalExceptionHandler.java
    ↓ (se erro, intercepta)
common/interceptor/RoleAuthorizationInterceptor.java
    ↓ (valida before, se escrita)
```

---

## 🔐 Fluxo de Autorização

```
RoleAuthorizationInterceptor (preHandle)
    ↓
    ├─ Verifica se é GET
    │   └─ Sim? → Passa (PUBLIC)
    │
    └─ Verifica se é POST/PUT/DELETE
        ├─ Sim? → Valida X-ROLE header
        │   ├─ X-ROLE = LIBRARIAN? → Passa
        │   └─ X-ROLE ≠ LIBRARIAN? → 403 Forbidden
        │
        └─ Não? → Passa
```

---

## 💾 Schema do Banco de Dados (H2)

```sql
-- LIBRARY_USERS TABLE
CREATE TABLE library_users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  role VARCHAR(50) NOT NULL  -- READER | LIBRARIAN
);

-- BOOKS TABLE
CREATE TABLE books (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL,
  author VARCHAR(255) NOT NULL,
  isbn VARCHAR(255) NOT NULL UNIQUE,
  published_date DATE NOT NULL,
  available BOOLEAN NOT NULL DEFAULT TRUE
);

-- LOANS TABLE
CREATE TABLE loans (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  book_id BIGINT NOT NULL,
  loan_date DATE NOT NULL,
  due_date DATE NOT NULL,
  return_date DATE,
  fine_amount DECIMAL(10,2),
  
  FOREIGN KEY (user_id) REFERENCES library_users(id),
  FOREIGN KEY (book_id) REFERENCES books(id)
);
```

---

## 🚀 Endpoints da API REST

### Books `/api/books`
```
GET    /api/books              → Listar todos
POST   /api/books              → Criar (requer X-ROLE: LIBRARIAN)
GET    /api/books/{id}         → Detalhes
PUT    /api/books/{id}         → Atualizar (requer X-ROLE: LIBRARIAN)
DELETE /api/books/{id}         → Remover (requer X-ROLE: LIBRARIAN)
```

### Users `/api/users`
```
GET    /api/users              → Listar todos
POST   /api/users              → Criar
GET    /api/users/{id}         → Detalhes
```

### Loans `/api/loans`
```
GET    /api/loans              → Listar todos
POST   /api/loans/borrow       → Emprestar livro
POST   /api/loans/{id}/return  → Devolver livro
```

---

## 📦 Dependências Maven

```xml
Spring Boot 3.2.2
├── spring-boot-starter-web         (REST, MVC)
├── spring-boot-starter-data-jpa    (Hibernate, JPA)
├── spring-boot-starter-validation  (Bean Validation)
├── spring-boot-starter-logging     (SLF4J, Logback)
├── h2                              (Database em memória)
└── spring-boot-devtools            (optional, development)
```

---

## 🔍 Annotations Principais Usado

### Spring Boot
- `@SpringBootApplication`
- `@RestController`, `@RequestMapping`
- `@Service`, `@Repository`
- `@Configuration`
- `@Bean`

### Spring Data JPA
- `@Entity`, `@Table`
- `@Id`, `@GeneratedValue`
- `@Column`, `@JoinColumn`
- `@ManyToOne`, `@OneToMany`
- `@Enumerated`

### Validation
- `@NotBlank`, `@NotNull`
- `@Email`
- `@Valid`

### HTTP/REST
- `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`
- `@PathVariable`, `@RequestBody`
- `@ControllerAdvice`, `@ExceptionHandler`

### Transações
- `@Transactional`
- `@Transactional(readOnly=true)`

---

## 📝 Linhas de Código Aproximadas

| Componente | LOC |
|-----------|-----|
| Controllers | ~150 |
| Services | ~250 |
| Entities | ~200 |
| Repositories | ~50 |
| DTOs | ~300 |
| Handlers/Interceptors | ~150 |
| Config | ~100 |
| Total Java | ~1,200 |

---

## ✅ Checklist de Entrega

- [x] Estrutura Maven completa
- [x] pom.xml com todas as dependências
- [x] application.yml configurado
- [x] 5 camadas arquiteturais bem definidas
- [x] 27 classes Java implementadas
- [x] CRUD completo de Livros
- [x] CRUD básico de Usuários
- [x] Sistema de Empréstimos e Devoluções
- [x] Cálculo automático de multas
- [x] Notificações via logs
- [x] Autorização por role (READER/LIBRARIAN)
- [x] Validação de dados (Bean Validation)
- [x] Tratamento centralizado de erros
- [x] Seed de dados inicial
- [x] Banco H2 em memória
- [x] Logs com SLF4J
- [x] DTOs para segurança
- [x] Sem Lombok
- [x] Sem bibliotecas extras
- [x] Código compilável (BUILD SUCCESS)
- [x] README completo com exemplos
- [x] ARCHITECTURE.md documentado
- [x] SUMMARY.md sumário
- [x] .gitignore
- [x] Comentários mínimos mas claros
- [x] Transações ACID (@Transactional)
- [x] Interceptor para autorização

---

## 🎓 Padrões de Design Utilizados

1. **Layered Architecture** - Separação em 5 camadas
2. **Repository Pattern** - Abstração de persistência
3. **Service Layer** - Orquestração de regras de negócio
4. **DTO Pattern** - Desacoplamento API/Modelo
5. **Adapter Pattern** - NotificationService
6. **Singleton Pattern** - Spring Beans
7. **Interceptor Pattern** - RoleAuthorizationInterceptor
8. **Observer Pattern** - Event listeners (Spring)
9. **Factory Pattern** - Spring Bean factory

---

## 💡 Highlights da Implementação

✨ **Arquitetura Modular** - Fácil de entender e manter
✨ **Separação de Responsabilidades** - Cada camada com seu propósito
✨ **Validação em Múltiplos Níveis** - Bean Validation + Custom
✨ **Segurança Implementada** - Autorização por role
✨ **Tratamento Robusto de Erros** - GlobalExceptionHandler
✨ **Transações ACID** - Consistência de dados
✨ **Notificações Extensíveis** - Fácil trocar implementação
✨ **Seed de Dados** - Demo pronta para uso
✨ **Logging Estruturado** - Rastreabilidade
✨ **Documentação Completa** - README + ARCHITECTURE

---

## 🚀 Próxima Execução

Para compilar e rodar:

```bash
cd /Users/programador-rocha/Downloads/demo-c4-model-main/Biblioteca/library-manager

# Compilar
mvn clean compile

# Executar
mvn spring-boot:run

# Acessar
http://localhost:8080/api/books
http://localhost:8080/h2-console
```

---

**Projeto: ✅ Concluído e Pronto para Demonstração**
