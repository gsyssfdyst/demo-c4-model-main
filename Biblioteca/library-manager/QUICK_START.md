# 🚀 Quick Start - Guia Rápido

Instruções para colocar a aplicação em funcionamento em menos de 5 minutos.

---

## 📋 Pré-requisitos

- ✅ Java 17+ instalado
- ✅ Maven 3.6+ instalado

**Verificar instalação:**
```bash
java -version
mvn -version
```

---

## 🔧 1. Compilar

```bash
cd /Users/programador-rocha/Downloads/demo-c4-model-main/Biblioteca/library-manager

# Limpar e compilar
mvn clean compile

# Ou diretamente compilar e empacotar (cria JAR)
mvn clean package
```

---

## ▶️ 2. Executar a Aplicação

### Opção A: Usando Maven
```bash
mvn spring-boot:run
```

### Opção B: Executar o JAR diretamente
```bash
java -jar target/library-manager-0.0.1-SNAPSHOT.jar
```

---

## ✅ 3. Verificar que Funcionou

Você verá no console:
```
=== Database initialized with sample data ===
Books: 3
Users: 2 (1 Reader, 1 Librarian)
H2 Console: http://localhost:8080/h2-console
==========================================
```

---

## 🌍 4. Acessar a Aplicação

### API REST
```
http://localhost:8080/api/books
http://localhost:8080/api/books/1
http://localhost:8080/api/users
http://localhost:8080/api/loans
```

### Banco de Dados (H2 Console)
```
http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:librarydb
Username: sa
Password: (deixar vazio)
```

---

## 🧪 5. Testar a API

### Listar Livros
```bash
curl http://localhost:8080/api/books
```

### Criar Livro (precisa de X-ROLE: LIBRARIAN)
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

### Listar Usuários
```bash
curl http://localhost:8080/api/users
```

### Emprestar um Livro
```bash
curl -X POST http://localhost:8080/api/loans/borrow \
  -H "Content-Type: application/json" \
  -d '{"userId": 1, "bookId": 1}'
```

### Devolver um Livro
```bash
curl -X POST http://localhost:8080/api/loans/1/return \
  -H "Content-Type: application/json"
```

---

## 📚 6. Dados Iniciais (Seed)

A aplicação já vem com dados pré-carregados:

### Livros
1. **Clean Code** by Robert C. Martin (ISBN: 978-0132350884)
2. **Design Patterns** by Gang of Four (ISBN: 978-0201633610)
3. **Refactoring** by Martin Fowler (ISBN: 978-0201485677)

### Usuários
1. **João Silva** (READER) - joao@example.com
2. **Maria Santos** (LIBRARIAN) - maria@example.com

---

## 🔒 7. Autorização

Use o header `X-ROLE` para simular autorização:

```bash
# Apenas LIBRARIAN pode criar/atualizar/deletar livros
-H "X-ROLE: LIBRARIAN"

# Válido: READER ou LIBRARIAN
-H "X-ROLE: READER"
```

**Respostas:**
- ✅ Operação de leitura (GET) → Permitida (qualquer role)
- ✅ Operação de escrita (POST/PUT/DELETE) com `X-ROLE: LIBRARIAN` → Permitida
- ❌ Operação de escrita com `X-ROLE: READER` → 403 Forbidden
- ❌ Operação de escrita sem X-ROLE → 403 Forbidden

---

## 📝 8. Logs

Os logs com informações importantes aparecem no console:

### Exemplo: Notificação de Multa
```
=== FineNotification ===
UserId: 1
LoanId: 1
FineAmount: R$ 16.00
Message: Late return detected. A fine of R$ 16.00 has been applied.
======================
```

---

## 📖 9. Documentação Completa

- **README.md** - Documentação completa com todos os exemplos
- **ARCHITECTURE.md** - Explicação detalhada da arquitetura
- **FILE_STRUCTURE.md** - Estrutura de pastas
- **SUMMARY.md** - Sumário do projeto

---

## 🛑 10. Parar a Aplicação

```bash
# No terminal onde a app está rodando
Ctrl + C
```

---

## 🆘 Troubleshooting

### Erro: "Port 8080 already in use"
```bash
# A porta 8080 já está em uso. Use outra porta:
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

### Erro: "Java version not compatible"
```bash
# Verifique se tem Java 17+
java -version

# Se estiver com versão mais antiga, faça update ou instale Java 17
```

### Erro: "Maven not found"
```bash
# Instale Maven
brew install maven  # macOS
# ou acesse https://maven.apache.org/download.cgi
```

---

## 📊 Endpoints de Teste Rápido

Use estes comandos para testar toda a funcionalidade:

```bash
# 1. Ver seed data
curl -s http://localhost:8080/api/books | jq .

# 2. Criar novo livro (com autorização)
curl -s -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -H "X-ROLE: LIBRARIAN" \
  -d '{"title":"Test","author":"Test","isbn":"123456789","publishedDate":"2026-03-02","available":true}' | jq .

# 3. Tentar criar sem autorização (403)
curl -s -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -H "X-ROLE: READER" \
  -d '{"title":"Test","author":"Test","isbn":"987654321","publishedDate":"2026-03-02","available":true}' | jq .

# 4. Emprestar livro
curl -s -X POST http://localhost:8080/api/loans/borrow \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"bookId":1}' | jq .

# 5. Ver empréstimos
curl -s http://localhost:8080/api/loans | jq .

# 6. Devolver livro
curl -s -X POST http://localhost:8080/api/loans/1/return | jq .
```

---

## 💡 Dicas

- Use `jq` para formatar JSON no terminal:
  ```bash
  curl http://localhost:8080/api/books | jq .
  ```

- Use o H2 Console para visualizar o banco em tempo real:
  ```
  http://localhost:8080/h2-console
  ```

- Abra DevTools (F12) no navegador para ver requisições HTTP

- Use Postman para testar com interface gráfica

---

## ✨ Próximos Passos

1. Explore os arquivos de documentação:
   - `README.md` - API completa
   - `ARCHITECTURE.md` - Design detalhado
   - `FILE_STRUCTURE.md` - Estrutura de pastas

2. Inspecione o código:
   - Controllers em `presentation/controller`
   - Services em `application/service`
   - Models em `domain/model`

3. Teste funcionalidades:
   - CRUD de livros
   - Autorização por role
   - Cálculo de multas
   - Notificações via logs

4. Modifique e estenda:
   - Adicione novos campos
   - Implemente novos receptarios
   - Crie novos endpoints

---

**🎉 Pronto! A aplicação está funcionando!**

Para mais detalhes, consulte a documentação completa nos arquivos `.md` do projeto.
