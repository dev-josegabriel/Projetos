# 📚 bibliotecaAPI

API REST para gestão de uma biblioteca simples, com cadastro de usuários, livros e controle de empréstimos/devoluções.

## Tecnologias

- Java 21
- Spring Boot 4.1.1
- Spring Data JPA / Hibernate
- PostgreSQL
- Lombok
- springdoc-openapi (Swagger)
- Maven

## Arquitetura

Projeto em camadas, organizado por responsabilidade:

```
com.josedev.bibliotecaAPI
├── controller   → endpoints REST
├── service      → regras de negócio
├── repository   → interfaces JPA (Spring Data)
├── model        → entidades (Usuario, Livro, Emprestimo)
├── dto          → objetos de entrada/saída (reservado para uso futuro)
└── exception    → exceções customizadas (reservado para uso futuro)
```

Fluxo de uma requisição: `Controller → Service → Repository → Banco`, e a resposta sobe pelo mesmo caminho.

## Modelagem

- **UsuarioEntity** — `id`, `nome`, `email` (único)
- **LivroEntity** — `id`, `titulo`, `autor`, `isbn` (único), `disponivel`
- **EmprestimoEntity** — `id`, `livro` (N:1), `usuario` (N:1), `dataEmprestimo`, `dataPrevistaDevolucao`, `dataDevolucaoEfetiva`

```
Usuario 1───N Emprestimo N───1 Livro
```

## Regras de negócio

- Um `Livro` só pode ser emprestado se `disponivel = true`.
- Ao criar um `Emprestimo`, o livro correspondente é marcado como indisponível (`disponivel = false`).
- Ao devolver (`dataDevolucaoEfetiva` preenchida), o livro volta a ficar disponível.
- Não é possível devolver um empréstimo que já foi devolvido.
- Cadastro de `Usuario` não permite `email` duplicado; cadastro de `Livro` não permite `isbn` duplicado.

## Como rodar

### Pré-requisitos

- Java 21
- PostgreSQL rodando localmente (ou ajuste a `url` em `application.yaml`)
- Um banco de dados criado (padrão: `biblioteca_db`)

### Variáveis de ambiente

A senha do banco não fica no código — configure antes de rodar:

| Variável | Obrigatória | Default |
|---|---|---|
| `DB_USERNAME` | Não | `postgres` |
| `DB_PASSWORD` | **Sim** | — |

PowerShell:
```powershell
$env:DB_PASSWORD="sua_senha"
```

### Executando

```bash
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8082`.

## Endpoints

### Usuário — `/v1/usuario`

| Método | Rota | Descrição |
|---|---|---|
| GET | `/v1/usuario` | Lista todos os usuários |
| GET | `/v1/usuario/{id}` | Busca usuário por id |
| POST | `/v1/usuario` | Cadastra usuário |
| DELETE | `/v1/usuario/{id}` | Remove usuário |

```json
// POST /v1/usuario
{
  "nome": "José Gabriel",
  "email": "jose@email.com"
}
```

### Livro — `/v1/livro`

| Método | Rota | Descrição |
|---|---|---|
| GET | `/v1/livro` | Lista todos os livros |
| GET | `/v1/livro/{id}` | Busca livro por id |
| POST | `/v1/livro` | Cadastra livro |
| DELETE | `/v1/livro/{id}` | Remove livro |

```json
// POST /v1/livro
{
  "titulo": "Clean Code",
  "autor": "Robert Martin",
  "isbn": "9780132350884"
}
```

### Empréstimo — `/v1/emprestimo`

| Método | Rota | Descrição |
|---|---|---|
| POST | `/v1/emprestimo?livroId={id}&usuarioId={id}` | Cria um empréstimo |
| POST | `/v1/emprestimo/{id}/devolver` | Registra a devolução |

## Autor

José Gabriel
