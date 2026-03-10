# 🚀 Executando a Web UI - Guia Prático

## ⚡ 5 Minutos para Iniciar

### 1️⃣ Compilar com Thymeleaf
```bash
cd library-manager
mvn clean package
```

Esperado:
```
[INFO] BUILD SUCCESS
```

---

### 2️⃣ Iniciar Aplicação
```bash
java -jar target/library-manager-0.0.1-SNAPSHOT.jar
```

Esperado no console:
```
Started LibraryManagerApplication in X.XXX seconds
```

---

### 3️⃣ Abrir no Navegador

**URL:** http://localhost:8080/web/books

---

## 🌐 Navegação Rápida

| Ação | URL |
|------|-----|
| 📚 Livros | http://localhost:8080/web/books |
| ➕ Novo Livro | http://localhost:8080/web/books/new |
| 👥 Usuários | http://localhost:8080/web/users |
| 🔄 Empréstimos | http://localhost:8080/web/loans |

---

## ✨ O que Você Pode Fazer

### 1. Visualizar Livros
- Tabela com todos os livros
- Status: Disponível ou Emprestado

### 2. Criar Livro
- Formulário com validação
- Campos: Título, Autor, ISBN, Data

### 3. Emprestar Livro
- Selecionar usuário
- Selecionar livro disponível
- Clique em "Emprestar"

### 4. Devolver Livro
- Clique em "Devolver" na tabela
- Livro volta a "Disponível"

---

## 🧪 Teste Rápido

1. Vá para http://localhost:8080/web/books
2. Clique em "Novo Livro"
3. Preencha:
   - Título: "Test Book"
   - Autor: "Test Author"
   - ISBN: "123-456"
   - Data: 2024-01-01
4. Clique "Salvar"
5. ✅ Livro criado!

---

## 📚 Arquivos de Documentação

- **WEB_UI_CODES.md** → Código completo
- **TESTING_GUIDE.md** → Testes detalhados
- **ESTRUTURA_WEB_UI.md** → Estrutura do projeto
- **WEB_UI_SUMMARY.md** → Resumo técnico

---

**Status:** ✅ Pronto para rodar!
