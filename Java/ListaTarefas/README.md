# Lista de Tarefas — Lista Encadeada

API REST em **Java + Spring Boot** com front-end em HTML/CSS/JS. As tarefas ficam guardadas numa **lista simplesmente encadeada implementada à mão** (sem `LinkedList` do Java), e a tela desenha a estrutura de dados funcionando.

## Como rodar

Precisa de **Java 17+** e **Maven** (ou IntelliJ).

- **IntelliJ:** abra a pasta (ou o `pom.xml`) como projeto, espere o Maven baixar as dependências e rode a classe `TarefasApplication`.
- **Terminal:** `mvn spring-boot:run`

Depois abra **http://localhost:8080** no navegador. Para rodar os testes: `mvn test`.

---

## Modelagem

### 1) Spring Initializr

| Campo | Valor |
|---|---|
| Project | Maven |
| Language | Java |
| Spring Boot | 3.3.5 |
| Group | br.com.josedev |
| Artifact | tarefas |
| Packaging | Jar |
| Java | 17 |

### 2) Dependências

- Spring Web
- Validation
- Spring Boot Starter Test (testes)

### 3) Estrutura de pacotes

```
br.com.josedev.tarefas
├── controller   → TarefaController (rotas da API)
├── service      → TarefaService (regras de negócio)
├── model        → Tarefa
├── dto          → TarefaRequest, TarefasResponse
├── estrutura    → No, ListaEncadeada (a estrutura de dados)
└── exception    → erros e tratamento (ApiExceptionHandler)
```

O front fica em `src/main/resources/static/index.html`, e o Spring entrega essa página em `/`.

### 4) Configuração do banco

**Não usa banco de dados, de propósito.** O foco do trabalho é a estrutura de dados: as tarefas vivem dentro da própria lista encadeada, na memória. Ao reiniciar a aplicação, a lista volta com 3 tarefas de exemplo.

### 5) Entidades

- **Tarefa:** id, titulo, concluida
- **No** (estrutura): valor, proximo
- **ListaEncadeada** (estrutura): inicio, fim, tamanho

### 6) Relacionamentos

- Cada **No** guarda uma **Tarefa** (`valor`) e aponta para o **próximo No**.
- A **ListaEncadeada** conhece o primeiro nó (`inicio`) e o último (`fim`).

```
inicio → [Tarefa 1 | •] → [Tarefa 2 | •] → [Tarefa 3 | •] → null
                                                  ↑ fim
```

### 7) Regras de negócio

1. O título é obrigatório e tem no máximo 100 caracteres (espaços nas pontas são removidos).
2. Não pode haver duas tarefas com o mesmo título, ignorando maiúsculas e minúsculas.
3. Sem posição informada, a tarefa entra no **fim** da lista. Posição `0` = **início**. Posição fora de `0..tamanho` dá erro.
4. Concluir uma tarefa já concluída **reabre** ela.
5. "Limpar concluídas" remove todas de uma vez e dá erro se não houver nenhuma.
6. Tarefa inexistente retorna 404.

---

## Rotas da API

| Método | Rota | O que faz |
|---|---|---|
| GET | `/api/tarefas` | Lista todas, na ordem da lista encadeada |
| POST | `/api/tarefas` | Adiciona. Body: `{"titulo": "...", "posicao": 0}` (`posicao` é opcional) |
| PUT | `/api/tarefas/{id}` | Edita o título. Body: `{"titulo": "..."}` |
| PATCH | `/api/tarefas/{id}/concluir` | Conclui ou reabre |
| DELETE | `/api/tarefas/{id}` | Remove |
| DELETE | `/api/tarefas/concluidas` | Remove todas as concluídas |

Todas devolvem o estado completo da lista: `{"tarefas": [...], "total": 3, "concluidas": 1, "pendentes": 2}`.

Erros vêm no formato `{"erro": "mensagem"}`.

## Custo de cada operação (Big-O)

| Operação | Custo | Por quê |
|---|---|---|
| Adicionar no início | O(1) | só troca o `inicio` |
| Adicionar no fim | O(1) | a lista guarda o `fim` |
| Inserir numa posição | O(n) | precisa andar até lá |
| Buscar / remover | O(n) | precisa andar procurando |
| Limpar concluídas | O(n) | uma única passada |
