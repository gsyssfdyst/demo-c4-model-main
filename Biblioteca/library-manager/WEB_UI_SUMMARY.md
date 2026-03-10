# Web UI - Thymeleaf + Bootstrap

## 📋 Resumo das Alterações

Adicionada uma camada de apresentação web (server-side rendered) ao projeto Library Manager usando Thymeleaf + Bootstrap 5.

### ✅ Funcionalidades Implementadas

- ✓ Listar, criar, visualizar e buscar livros via UI
- ✓ Listar usuários via UI
- ✓ Emprestar e devolver livros via UI
- ✓ Validação de formulários com Bean Validation
- ✓ Mensagens de sucesso/erro com flash attributes
- ✓ Tabelas responsivas com Bootstrap 5
- ✓ Navbar de navegação
- ✓ Separação de rotas REST (/api/**) e Web UI (/web/**)
- ✓ Autorização apenas para /api/**, não para /web/**

---

## 📁 Arquivos Criados

### 1. DTOs (common/dto)
```
src/main/java/com/biblioteca/common/dto/
├── BookCreateRequestDTO.java           (NOVO)
└── LoanListItemDTO.java                (NOVO)
```

### 2. Controllers (presentation/controller)
```
src/main/java/com/biblioteca/presentation/controller/
└── WebPageController.java              (NOVO)
```

### 3. Templates Thymeleaf (templates)
```
src/main/resources/templates/
├── layout.html                         (NOVO)
├── books.html                          (NOVO)
├── book-new.html                       (NOVO)
├── users.html                          (NOVO)
└── loans.html                          (NOVO)
```

---

## 📝 Arquivos Modificados

### 1. pom.xml
**Alteração:** Adicionada dependência Thymeleaf
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

### 2. src/main/java/com/biblioteca/common/interceptor/RoleAuthorizationInterceptor.java
**Alteração:** Modificado preHandle() para não aplicar validação de role em rotas /web/**

---

## 🚀 Como Usar

### 1. Compilar
```bash
cd library-manager
mvn clean compile
```

### 2. Executar
```bash
java -jar target/library-manager-0.0.1-SNAPSHOT.jar
```

### 3. Acessar a UI
```
http://localhost:8080/web/books    (Lista de livros)
http://localhost:8080/web/users    (Lista de usuários)
http://localhost:8080/web/loans    (Empréstimos)
```

---

## 📊 Rotas da Web UI

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | / | Redireciona para /web/books |
| GET | /web/books | Lista todos os livros |
| GET | /web/books/new | Formulário novo livro |
| POST | /web/books | Cria novo livro |
| GET | /web/users | Lista usuários |
| GET | /web/loans | Lista empréstimos + formulário |
| POST | /web/loans/borrow | Emprestar livro |
| POST | /web/loans/{id}/return | Devolver livro |

---

## 🔒 Segurança

- ✓ Rotas /api/** mantêm validação de header X-ROLE
- ✓ Rotas /web/** não aplicam validação de role (public)
- ✓ Validação de formulários com @Valid + BindingResult
- ✓ Templates escapam XSS automaticamente (Thymeleaf)

---

## 📖 Métodos Utilizados dos Services

### BookService
- `getAllBooks()` - Retorna lista de BookDTO
- `getBookById(Long id)` - Retorna detalhes do livro
- `createBook(BookDTO dto)` - Cria novo livro

### LibraryUserService
- `getAllUsers()` - Retorna lista de LibraryUserDTO
- `getUserById(Long id)` - Retorna detalhes do usuário

### LoanService
- `getAllLoans()` - Retorna lista de LoanDTO
- `borrowBook(BorrowRequestDTO dto)` - Emprestar livro
- `returnBook(Long id)` - Devolver livro

---

## 🎨 Design da UI

### Tech Stack
- **Framework:** Spring Boot 3 (Thymeleaf)
- **Styling:** Bootstrap 5 (CDN)
- **Validation:** Bean Validation (@NotBlank, @NotNull, etc)
- **Responsive:** Mobile-first design com Bootstrap

### Templates
1. **layout.html** - Base com navbar e footer (não usado como fragmento, mas como referência)
2. **books.html** - Tabela de livros com link para novo livro
3. **book-new.html** - Formulário de cadastro com validação
4. **users.html** - Tabela simples de usuários
5. **loans.html** - Form + tabela de empréstimos

---

## ✨ Recursos Adicionais

- Flash messages para feedback ao usuário (success/error)
- Botão "Devolver" só aparece se returnDate for nulo
- Tabelas responsivas com Bootstrap
- Validação lado do servidor (Bean Validation)
- Mensagens de erro inline no formulário

---

## 🔗 Endpoints REST Mantidos

Todos os endpoints de API REST (/api/**) continuam funcionando normalmente:

```bash
GET    /api/books              POST   /api/loans/borrow
GET    /api/books/{id}         POST   /api/loans/{id}/return
POST   /api/books              GET    /api/loans
PUT    /api/books/{id}
DELETE /api/books/{id}
GET    /api/users
POST   /api/users
```

---

## ✅ Testes

A aplicação foi compilada com sucesso:
```
[INFO] BUILD SUCCESS
```

Pronta para rodar e demonstrar o MVP na apresentação!

---

## 🎯 Próximas Melhorias (Opcional)

- [ ] CRUD completo de usuários na UI
- [ ] Paginação de tabelas
- [ ] Filtros avançados
- [ ] Temas dark/light
- [ ] Internacionalização (i18n)
- [ ] Gráficos de estatísticas
