# 🧪 Guia de Testes - Web UI

## 📋 Roteiros de Teste

### 1️⃣ Teste de Inicialização

**Objetivo:** Confirmar que a aplicação inicia com sucesso com as novas dependências.

**Passos:**
1. Limpar e reconstruir o projeto:
   ```bash
   cd library-manager
   mvn clean package
   ```

2. Executar a aplicação:
   ```bash
   java -jar target/library-manager-0.0.1-SNAPSHOT.jar
   ```

3. Verificar no console:
   - ✅ Nenhuma mensagem de erro
   - ✅ Banco de dados H2 criado com sucesso
   - ✅ Mensagem: "Started LibraryManagerApplication in ... seconds"

**Critério de Sucesso:** Aplicação iniciada sem erros

---

### 2️⃣ Teste de Rotas Básicas

**Objetivo:** Verificar se as rotas da web UI estão acessíveis.

| Rota | Tipo | Esperado | Status |
|------|------|----------|--------|
| `GET /` | Navegação | Redireciona para `/web/books` | ⏳ |
| `GET /web/books` | Página | Exibe tabela de livros | ⏳ |
| `GET /web/books/new` | Página | Exibe formulário de criar livro | ⏳ |
| `GET /web/users` | Página | Exibe tabela de usuários | ⏳ |
| `GET /web/loans` | Página | Exibe empréstimos + formulário | ⏳ |

**Teste no Browser:**
1. Acessar http://localhost:8080
2. Verificar cada rota:
   ```
   http://localhost:8080/web/books
   http://localhost:8080/web/books/new
   http://localhost:8080/web/users
   http://localhost:8080/web/loans
   ```

**Critério de Sucesso:** Todas as páginas carregam sem erro 404

---

### 3️⃣ Teste de Dados Iniciais

**Objetivo:** Confirmar que a página exibe dados do banco de dados H2.

**Passos em /web/books:**
- [ ] Tabela carrega sem erro
- [ ] Pelo menos 5 livros aparecem (dados de inicialização)
- [ ] Colunas exibem: ID, Título, Autor, ISBN, Data Publicação, Status

**Passos em /web/users:**
- [ ] Tabela carrega sem erro
- [ ] Pelo menos 3 usuários aparecem
- [ ] Roles (READER/LIBRARIAN) aparecem como badges

**Passos em /web/loans:**
- [ ] Tabela de empréstimos carrega
- [ ] Formulário de empréstimo com selects aparece
- [ ] Selects de usuários e livros preenchidos

**Critério de Sucesso:** Todos os dados carregam corretamente

---

### 4️⃣ Teste de Criação de Livro

**Objetivo:** Validar formulário e criação via web UI.

**Cenário 1 - Validação**

1. Acessar http://localhost:8080/web/books/new
2. Deixar campos vazios e clicar "Salvar":
   - [ ] Mensagens de erro aparecem (campos obrigatórios)
   - [ ] Formulário não submit

3. Preencher apenas título e clicar "Salvar":
   - [ ] Erros aparecem para autor e ISBN
   - [ ] Formulário não submit

**Cenário 2 - Criação com sucesso**

1. Preencher todos os campos:
   - Título: "Clean Code"
   - Autor: "Robert C. Martin"
   - ISBN: "978-0132350884"
   - Data: 01/08/2008

2. Clicar "Salvar"
   - [ ] Mensagem de sucesso em verde aparece: "Livro criado com sucesso!"
   - [ ] Redirecionado para /web/books
   - [ ] Novo livro aparece na tabela
   - [ ] Status: "Disponível"

**Critério de Sucesso:** Livro criado e exibido na tabela

---

### 5️⃣ Teste de Empréstimo (Borrow)

**Objetivo:** Validar fluxo de empréstimo de livro.

**Passos:**

1. Acessar http://localhost:8080/web/loans
2. No formulário de empréstimo:
   - [ ] Select de usuários carregado
   - [ ] Select de livros mostra apenas livros disponíveis
   - [ ] Botão "Emprestar" presente

3. Selecionar:
   - Usuário: primeiro usuário da lista
   - Livro: primeiro livro disponível

4. Clicar "Emprestar":
   - [ ] Mensagem de sucesso: "Livro emprestado com sucesso!"
   - [ ] Redirecionado para /web/loans
   - [ ] Novo empréstimo aparece na tabela
   - [ ] Livro agora aparece como "Emprestado" em /web/books

**Validações:**
- [ ] Livro emprestado sai da lista de disponíveis
- [ ] Datas de empréstimo/devolução preenchidas corretamente
- [ ] Status "returnDate = vazio" para empréstimos ativos

**Critério de Sucesso:** Empréstimo criado e visível na tabela

---

### 6️⃣ Teste de Devolução (Return)

**Objetivo:** Validar fluxo de devolução de livro.

**Passos:**

1. Na página /web/loans, localize um empréstimo ativo (sem "Devolvido"):
   - [ ] Botão "Devolver" presente

2. Clicar "Devolver":
   - [ ] Confirmação do Thymeleaf (mensagem de confirmação)
   - [ ] Mensagem de sucesso: "Livro devolvido com sucesso!"
   - [ ] Empréstimo agora mostra data de devolução
   - [ ] Botão muda para "Devolvido" (desabilitado)

3. Verificar em /web/books:
   - [ ] Livro devolvido agora mostra status "Disponível"
   - [ ] Livro reaparece disponível para empréstimo

**Critério de Sucesso:** Livro devolvido, status atualizado

---

### 7️⃣ Teste de Segurança REST

**Objetivo:** Confirmar que endpoints /api/** ainda requerem X-ROLE.

**Teste 1 - GET sem header (deve funcionar)**
```bash
curl http://localhost:8080/api/books
```
Esperado: 200 OK com lista de livros

**Teste 2 - POST sem header (deve falhar)**
```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{"title":"Test","author":"Test","isbn":"123","publishedDate":"2024-01-01"}'
```
Esperado: 403 Forbidden - "X-ROLE header is required"

**Teste 3 - POST com X-ROLE=READER (deve falhar)**
```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -H "X-ROLE: READER" \
  -d '{"title":"Test","author":"Test","isbn":"123","publishedDate":"2024-01-01"}'
```
Esperado: 403 Forbidden - "Only LIBRARIAN role can perform this operation"

**Teste 4 - POST com X-ROLE=LIBRARIAN (deve funcionar)**
```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -H "X-ROLE: LIBRARIAN" \
  -d '{"title":"Test","author":"Test","isbn":"123","publishedDate":"2024-01-01"}'
```
Esperado: 201 Created com novo livro

**Critério de Sucesso:** /api/** requer X-ROLE, /web/** não requer

---

### 8️⃣ Teste de Styling Bootstrap

**Objetivo:** Verificar que Bootstrap 5 está funcionando corretamente.

**Verificações Visuais:**

- [ ] Tabelas têm estilos Bootstrap (bordas, padding, hover effect)
- [ ] Formulários têm estilos Bootstrap (inputs com padding, botões formatados)
- [ ] Badges de role (READER/LIBRARIAN) aparecem coloridos
- [ ] Badges de status (Disponível/Emprestado) aparecem coloridos
- [ ] Página é responsiva (testar em diferentes tamanhos de tela)
- [ ] Navbar está presente em todas as páginas (layout)
- [ ] Mensagens de sucesso/erro aparecem com cores (verde/vermelho)
- [ ] Links "Voltar" funcionam

**Critério de Sucesso:** UI visualmente apresentável com Bootstrap

---

### 9️⃣ Teste de Mensagens Flash

**Objetivo:** Validar que flash messages aparecem corretamente.

**Sucesso (verde):**
- [ ] Criar livro → "Livro criado com sucesso!"
- [ ] Emprestar livro → "Livro emprestado com sucesso!"
- [ ] Devolver livro → "Livro devolvido com sucesso!"

**Erro (vermelho):**
- [ ] Tentar criar livro com dados inválidos → Erro exibido
- [ ] Tentar emprestar livro com dados inválidos → Erro exibido

**Critério de Sucesso:** Mensagens aparecem e desaparecem após refresh

---

### 🔟 Teste Integrado (Happy Path)

**Objetivo:** Simular fluxo completo de uso.

**Cenário Completo:**
1. Abrir http://localhost:8080
2. Visualizar livros em /web/books
3. Criar novo livro via /web/books/new
4. Visualizar usuários em /web/users
5. Emprestar um livro em /web/loans
6. Devolver o livro
7. Verificar que livro voltou a estar disponível
8. Testar endpoint REST /api/books com curl

**Critério de Sucesso:** Todo fluxo funciona sem erros

---

## 📊 Checklist de Testes

```
✅ Setup e Compilação
  ├─ [ ] mvn clean package sucesso
  ├─ [ ] java -jar inicia sem erros
  └─ [ ] Nenhuma exceção no console

✅ Rotas Web
  ├─ [ ] GET / redireciona para /web/books
  ├─ [ ] GET /web/books carrega
  ├─ [ ] GET /web/books/new carrega
  ├─ [ ] GET /web/users carrega
  └─ [ ] GET /web/loans carrega

✅ Exibição de Dados
  ├─ [ ] Tabela books exibe livros
  ├─ [ ] Tabela users exibe usuários
  ├─ [ ] Tabela loans exibe empréstimos
  └─ [ ] Selects preenchidos com dados

✅ Criação de Livro
  ├─ [ ] Validação funciona (campos obrigatórios)
  ├─ [ ] Livro criado é exibido na tabela
  ├─ [ ] Status inicial: "Disponível"
  └─ [ ] Mensagem de sucesso exibida

✅ Empréstimo
  ├─ [ ] Formulário exibe usuários e livros
  ├─ [ ] Empréstimo criado é exibido na tabela
  ├─ [ ] Livro emprestado sai de "Disponível"
  └─ [ ] Datas preenchidas corretamente

✅ Devolução
  ├─ [ ] Botão "Devolver" aparece
  ├─ [ ] Livro é marcado como devolvido
  ├─ [ ] Livro volta a "Disponível"
  └─ [ ] Mensagem de sucesso exibida

✅ Segurança
  ├─ [ ] /api/** requer X-ROLE header
  ├─ [ ] /web/** funciona sem headers
  ├─ [ ] READER não pode fazer POST
  └─ [ ] LIBRARIAN pode fazer POST

✅ UI/Styling
  ├─ [ ] Bootstrap 5 carregado
  ├─ [ ] Tabelas formatadas
  ├─ [ ] Formulários formatados
  ├─ [ ] Responsivo (mobile/desktop)
  └─ [ ] Cores e badges OK

✅ Mensagens
  ├─ [ ] Sucesso em verde
  ├─ [ ] Erro em vermelho
  ├─ [ ] Validação inline OK
  └─ [ ] Flash messages desaparecem após refresh
```

---

## 🚨 Troubleshooting

| Problema | Causa | Solução |
|----------|-------|---------|
| 404 em /web/books | WebPageController não registrado | Verificar se está em pacote correto (presentation/controller) |
| Thymeleaf não processa | Dependency missing | Verificar pom.xml tem spring-boot-starter-thymeleaf |
| Form com erro 403 | X-ROLE header requerido | Verificar interceptor bypass em RoleAuthorizationInterceptor |
| Dados não exibem | Service retorna null | Testar: curl http://localhost:8080/api/books |
| Bootstrap não carrega | CDN bloqueado | Verificar conexão internet, ou adicionar Bootstrap local |
| Validação não funciona | BindingResult não usado | Verificar @Valid anotação no controller |

---

## ✅ Próximos Passos

1. **Testar tudo acima** ← Você está aqui
2. Reportar bugs encontrados
3. Otimizar CSS se necessário
4. Adicionar funcionalidades extras se solicitado
5. Documentar fluxos finais

Boa sorte! 🎉
