# Como Rodar o Projeto

Este guia explica passo a passo como executar o projeto Sistema de Gestão de Biblioteca (Backend + Frontend + Banco de Dados) localmente, útil para quando o projeto for baixado em uma nova máquina.

## Pré-requisitos

Para rodar este projeto na sua máquina, você vai precisar das seguintes ferramentas instaladas:

1. [Docker](https://docs.docker.com/get-docker/) e [Docker Compose](https://docs.docker.com/compose/install/) (para o banco de dados)
2. [Java JDK 17+](https://adoptium.net/) (para o backend Spring Boot)
3. [Node.js 18+](https://nodejs.org/) e `npm` (para o frontend Vite/React)
4. (Opcional) Git para clonar o repositório

---

## Passo 1: Iniciar o Banco de Dados

O banco de dados do projeto é em PostgreSQL e já está configurado via Docker Compose.

Abra o terminal na **raiz do projeto** (`demo-c4-model-main`) e execute o seguinte comando:

```bash
docker compose up -d
```

> **Atenção:** O arquivo `docker-compose.yml` inicia o PostgreSQL expondo-o na **porta 5433** por padrão (mapeado para a porta interna 5432) com o usuário `postgres` e senha `password`.

Para parar o banco de dados posteriormente, use: `docker compose stop` ou `docker compose down`.

---

## Passo 2: Rodar o Backend API (Spring Boot)

Após o banco de dados estar rodando e os contêineres inicializados com sucesso, subiremos o backend da API construído com Java/Spring Boot.

1. No terminal, navegue até a pasta `backend`:
   ```bash
   cd backend
   ```

2. Se você estiver no macOS/Linux, primeiro garanta permissão de execução do Maven Wrapper:
   ```bash
   chmod +x mvnw
   ```

3. Agora, execute o Spring Boot pelo terminal usando o Maven Wrapper embutido:
   ```bash
   # no Mac / Linux
   ./mvnw spring-boot:run
   
   # no Windows (Prompt de Comando/PowerShell)
   mvnw.cmd spring-boot:run
   ```

A aplicação deverá baixar as dependências e subir. O backend estará disponível localmente em:
👉 **http://localhost:8080**

---

## Passo 3: Rodar o Frontend Web (React + Vite)

Com a API funcionando, agora é hora de rodar a interface de usuário web.

1. Abra uma nova aba ou janela no seu terminal (para não fechar a execução do backend).
2. Navegue até a pasta `frontend-web` na raiz do projeto:
   ```bash
   cd frontend-web
   ```

3. Instale as dependências JavaScript do projeto usando o NPM:
   ```bash
   npm install
   ```

4. Após o final da instalação, inicie o servidor de desenvolvimento do Vite:
   ```bash
   npm run dev
   ```

O frontend deverá inicializar com sucesso e a interface da aplicação estará rodando localmente em:
👉 **http://localhost:5173**

Abra esse endereço no seu navegador favorito para começar a usar a aplicação!

---

## Possíveis Erros e Soluções (Troubleshooting)

- **Porta em Uso**: Caso as portas `8080` (Backend), `5173` (Frontend) ou `5433` (DB) já estejam sendo usadas, o seu serviço falhará ao iniciar. Você precisará parar o serviço na sua máquina que está utilizando essas portas ou reconfigurar as portas na aplicação (`application.properties` para o backend, `vite.config.js` para o front, ou `docker-compose.yml` para o banco de dados).
- **Dados do Banco**: O Spring Data JPA do backend deve criar o schema necessário das tabelas automaticamente ao subir conectado ao Docker do banco de dados na URL estipulada.

