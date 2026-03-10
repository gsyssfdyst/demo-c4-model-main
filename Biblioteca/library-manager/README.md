# Library Manager MVP

Projeto Maven Spring Boot 3 demonstrando um Sistema de Gestão de Biblioteca com arquitetura **Monólito Modular em Camadas** (Presentation → Application → Domain → Persistence → Integration).

## Características

- **Java 17** + Spring Boot 3
- **Banco H2** em memória para facilitar demonstrações
- **Arquitetura em 5 camadas** com separação clara de responsabilidades
- **Spring Data JPA** para persistência
- **Bean Validation** para validação de inputs
- **Autorização por roles** via header customizado (X-ROLE)
- **Notificações simuladas** via logs (SLF4J)
- **Tratamento global de exceções** com @ControllerAdvice
- **Seed de dados** automático (CommandLineRunner)

## Requisitos

- Java 17+
- Maven 3.6+
- Nenhuma dependência externa além das do Spring Boot

## Estrutura de Pastas

```
library-manager/
├── pom.xml
├── src/main
│   ├── java/com/biblioteca
│   │   ├── presentation/controller          (REST Controllers)
│   │   ├── application/service              (Casos de uso)
│   │   ├── domain/model                     (Entidades JPA + Enums)
│   │   │   └── enums                        (UserRole)
│   │   ├── persistence/repository           (JPA Repositories)
│   │   ├── integration/notification         (Adapter de notificações)
│   │   ├── common
│   │   │   ├── exception                    (Exceções customizadas)
│   │   │   ├── handler                      (GlobalExceptionHandler)
│   │   │   ├── interceptor                  (RoleAuthorizationInterceptor)
│   │   │   ├── config                       (Configuration beans)
│   │   │   └── dto                          (Transfer Objects)
│   │   └── LibraryManagementApplication.java
│   └── resources
│       ├── application.yml
│       └── logback-spring.xml
└── README.md
```

## Como Executar

### 1. Compilar o projeto
```bash
cd library-manager
mvn clean compile
```

### 2. Executar a aplicação
```bash
mvn spring-boot:run
```

Ou, construir e executar o JAR:
```bash
mvn clean package
java -jar target/library-manager-0.0.1-SNAPSHOT.jar
```

A aplicação iniciará em `http://localhost:8080`

### 3. Acessar H2 Console (banco de dados)
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:librarydb`
- Credenciais: username `sa`, senha em branco

## API REST

### Base URL
```
http://localhost:8080/api
```

## Exemplos de Requisições

### 1. Listar Livros (GET - sem autenticação)
```bash
curl -X GET http://localhost:8080/api/books \
  -H "Content-Type: application/json"
```

**Resposta (200 OK):**
```json
[
  {
    "id": 1,
    "title": "Clean Code",
    "author": "Robert C. Martin",
    "isbn": "978-0132350884",
    "publishedDate": "2008-08-01",
    "available": true
  },
  ...
]
```

---

### 2. Criar Livro (POST - requer X-ROLE LIBRARIAN)
```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -H "X-ROLE: LIBRARIAN" \
  -d '{
    "title": "Spring in Action",
    "author": "Craig Walls",
    "isbn": "978-1617294945",
    "publishedDate": "2018-10-01",
    "available": true
  }'
```

**Resposta (201 Created):**
```json
{
  "id": 4,
  "title": "Spring in Action",
  "author": "Craig Walls",
  "isbn": "978-1617294945",
  "publishedDate": "2018-10-01",
  "available": true
}
```

---

### 3. Tentar Criar Livro SEM Autorização (403 Forbidden)
```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -H "X-ROLE: READER" \
  -d '{
    "title": "Java Concurrency in Practice",
    "author": "Brian Goetz",
    "isbn": "978-0321349606",
    "publishedDate": "2006-05-19",
    "available": true
  }'
```

**Resposta (403 Forbidden):**
```json
{
  "status": 403,
  "error": "Forbidden",
  "message": "Only LIBRARIAN role can perform this operation"
}
```

---

### 4. Obter Detalhes de um Livro (GET)
```bash
curl -X GET http://localhost:8080/api/books/1 \
  -H "Content-Type: application/json"
```

**Resposta (200 OK):**
```json
{
  "id": 1,
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "isbn": "978-0132350884",
  "publishedDate": "2008-08-01",
  "available": true
}
```

---

### 5. Atualizar Livro (PUT - requer X-ROLE LIBRARIAN)
```bash
curl -X PUT http://localhost:8080/api/books/1 \
  -H "Content-Type: application/json" \
  -H "X-ROLE: LIBRARIAN" \
  -d '{
    "title": "Clean Code - 2nd Edition",
    "author": "Robert C. Martin",
    "isbn": "978-0132350884",
    "publishedDate": "2008-08-01",
    "available": true
  }'
```

**Resposta (200 OK):**
```json
{
  "id": 1,
  "title": "Clean Code - 2nd Edition",
  "author": "Robert C. Martin",
  "isbn": "978-0132350884",
  "publishedDate": "2008-08-01",
  "available": true
}
```

---

### 6. Deletar Livro (DELETE - requer X-ROLE LIBRARIAN)
```bash
curl -X DELETE http://localhost:8080/api/books/4 \
  -H "X-ROLE: LIBRARIAN"
```

**Resposta (204 No Content)**

---

### 7. Listar Usuários (GET)
```bash
curl -X GET http://localhost:8080/api/users \
  -H "Content-Type: application/json"
```

**Resposta (200 OK):**
```json
[
  {
    "id": 1,
    "name": "João Silva",
    "email": "joao@example.com",
    "role": "READER"
  },
  {
    "id": 2,
    "name": "Maria Santos",
    "email": "maria@example.com",
    "role": "LIBRARIAN"
  }
]
```

---

### 8. Criar Usuário (POST)
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Pedro Costa",
    "email": "pedro@example.com",
    "role": "READER"
  }'
```

**Resposta (201 Created):**
```json
{
  "id": 3,
  "name": "Pedro Costa",
  "email": "pedro@example.com",
  "role": "READER"
}
```

---

### 9. Listar Empréstimos (GET)
```bash
curl -X GET http://localhost:8080/api/loans \
  -H "Content-Type: application/json"
```

**Resposta (200 OK):**
```json
[]
```

---

### 10. Realizar Empréstimo (POST - Borrow)
```bash
curl -X POST http://localhost:8080/api/loans/borrow \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "bookId": 1
  }'
```

**Resposta (201 Created):**
```json
{
  "id": 1,
  "userId": 1,
  "bookId": 1,
  "loanDate": "2026-03-02",
  "dueDate": "2026-03-16",
  "returnDate": null,
  "fineAmount": null
}
```

---

### 11. Devolver Livro DENTRO do Prazo (POST - Return)
```bash
curl -X POST http://localhost:8080/api/loans/1/return \
  -H "Content-Type: application/json"
```

**Resposta (200 OK):**
```json
{
  "id": 1,
  "userId": 1,
  "bookId": 1,
  "loanDate": "2026-03-02",
  "dueDate": "2026-03-16",
  "returnDate": "2026-03-02",
  "fineAmount": "0.00"
}
```

---

### 12. Devolver Livro COM ATRASO (simulação de multa)

**Nota:** Para testar multa, você precisaria manipular as datas no banco H2 ou usar uma nova requisição de empréstimo e retorno. Exemplo de simulated late return com output esperado:

```json
{
  "id": 1,
  "userId": 1,
  "bookId": 1,
  "loanDate": "2026-02-16",
  "dueDate": "2026-03-02",
  "returnDate": "2026-03-10",
  "fineAmount": "16.00"
}
```

**Console Log:**
```
=== FineNotification ===
UserId: 1
LoanId: 1
FineAmount: R$ 16.00
Message: Late return detected. A fine of R$ 16.00 has been applied.
======================
```

---

### 13. Tentar Emprestar Livro Indisponível (400 Bad Request)
```bash
curl -X POST http://localhost:8080/api/loans/borrow \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "bookId": 1
  }'
```

**Resposta (400 Bad Request):**
```json
{
  "timestamp": "2026-03-02T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Book is not available for borrowing",
  "path": "/api/loans/borrow"
}
```

---

## Detalhes de Implementação

### Camadas e Responsabilidades

1. **Presentation Layer** (presentation/controller)
   - REST Controllers: BookController, LibraryUserController, LoanController
   - Responsável apenas por receber e enviar HTTP responses
   - Delega toda lógica aos Services

2. **Application Layer** (application/service)
   - BookService, LibraryUserService, LoanService
   - Orquestra a lógica de negócio
   - Gerencia transações (@Transactional)
   - Chama Repositories e Adapters

3. **Domain Layer** (domain/model)
   - Entidades JPA: Book, LibraryUser, Loan
   - Enums: UserRole
   - Sem dependências de frameworks

4. **Persistence Layer** (persistence/repository)
   - JPA Repositories: BookRepository, LibraryUserRepository, LoanRepository
   - Apenas interfaces, Spring gera implementações

5. **Integration Layer** (integration/notification)
   - NotificationService: adapter para enviar notificações
   - Implementação atual: logs ao console
   - Fácil substituir por SMS, email, etc.

### Segurança

- **Autorização por Role:** Header `X-ROLE` obrigatório em operações de escrita (POST, PUT, DELETE)
- **Valores válidos:** `READER`, `LIBRARIAN`
- **Apenas LIBRARIAN** pode criar, atualizar ou deletar livros
- **GET (leitura)** é permitido para todas as roles
- Implementado via `RoleAuthorizationInterceptor` em `common/interceptor`

### Validação

- Bean Validation annotations: @NotBlank, @Email, @NotNull
- Erros retornam JSON com detalhes do campo
- Validação em DTOs de requisição

### Multas por Atraso

- **Período default:** 14 dias
- **Taxa:** R$ 2.00 por dia de atraso
- Calculada automaticamente no retorno
- Notificação disparada via `NotificationService`

### Tratamento de Erros

- GlobalExceptionHandler com @ControllerAdvice
- Todos os erros retornam JSON: timestamp, status, error, message, path
- Exceções customizadas: ResourceNotFoundException, BookNotAvailableException

## Dependências do Projeto

```xml
<dependencies>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
  </dependency>
  <dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
  </dependency>
</dependencies>
```

## Seed de Dados

Ao iniciar a aplicação, 3 livros e 2 usuários são criados automaticamente:

### Livros
1. Clean Code - Robert C. Martin (ISBN: 978-0132350884)
2. Design Patterns - Gang of Four (ISBN: 978-0201633610)
3. Refactoring - Martin Fowler (ISBN: 978-0201485677)

### Usuários
1. João Silva (READER) - joao@example.com
2. Maria Santos (LIBRARIAN) - maria@example.com

Use o H2 Console para visualizar os dados ou a API para consultas.

## Notas de Desenvolvimento

- **Sem Lombok:** código com getters/setters explícitos
- **Sem Spring Security:** autorização simplificada via header customizado
- **H2 em Memória:** dados perdidos ao desligar a aplicação
- **DDL Auto:** `create-drop` - tabelas criadas e droppadas a cada execução
- **Sem histórico de build:** .gitignore padrão do Maven

## Próximos Passos (Fora do MVP)

- Integração com banco relacional real (PostgreSQL, MySQL)
- Spring Security com JWT
- Paginação e filtros avançados
- Testes unitários e integração
- CI/CD pipeline
- Docker e containerização
- API Documentation com Swagger/OpenAPI
- Cache com Redis
- Auditoria de operações

## Autor

Projeto MVP - Sistema de Gestão de Biblioteca com Arquitetura Monólito Modular em Camadas

## Licença

MIT
