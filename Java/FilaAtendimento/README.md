# Fila de Atendimento com Prioridade

Sistema de triagem para a recepção de uma clínica: **emergências** são chamadas antes dos **prioritários** (idosos, gestantes, PCD), e estes antes dos **normais**. Quem tem a mesma prioridade é chamado por ordem de chegada.

API REST em **Java + Spring Boot** com front-end em HTML/CSS/JS. As pessoas ficam guardadas numa **fila prioritária feita com lista encadeada** (sem `PriorityQueue` nem `LinkedList` do Java), e a tela desenha a estrutura funcionando.

## Problema que resolve

Na recepção de uma clínica, quem está mais grave precisa ser atendido antes, mesmo tendo chegado depois. A fila se organiza sozinha: cada pessoa entra na posição certa, e o próximo a ser chamado é sempre o primeiro da fila.

## Como rodar

Precisa de **Java 17+** e **Maven** (ou IntelliJ).

- **IntelliJ:** abra a pasta (ou o `pom.xml`), espere o Maven baixar as dependências e rode a classe `FilaApplication`.
- **Terminal:** `mvn spring-boot:run`

Depois abra **http://localhost:8080**. Para rodar os testes: `mvn test`.

---

## Modelagem

### 1) Spring Initializr

| Campo | Valor |
|---|---|
| Project | Maven |
| Language | Java |
| Spring Boot | 3.3.5 |
| Group | br.com.josedev |
| Artifact | fila-atendimento |
| Packaging | Jar |
| Java | 17 |

### 2) Dependências

Spring Web e Validation. Sem banco de dados: os dados ficam na própria estrutura, em memória.

### 3) Estrutura de pacotes

`estrutura` (fila prioritária), `model`, `dto`, `service`, `controller`, `exception`.

### 4) Banco

Não há. A fila é a "base de dados" (se o servidor reiniciar, volta ao estado inicial).

### 5) Entidade

**Atendimento:** id, senha, nome, prioridade, chegada.

### 6) Relacionamentos

Nenhum. Cada nó da fila aponta para o próximo.

### 7) Regras de negócio

- Nome obrigatório (até 60 caracteres) e prioridade obrigatória.
- Emergência (1) passa na frente de prioritário (2), que passa na frente de normal (3).
- Mesma prioridade: ordem de chegada.
- A mesma pessoa não entra duas vezes (ignora maiúsculas).
- Chamar o próximo com a fila vazia dá erro 409; cancelar quem não está na fila dá 404.

## Estrutura de dados

`FilaPrioritaria` é uma lista encadeada **sempre ordenada** (guarda `inicio`, `fim` e `tamanho`):

- **Chamar o próximo:** tira o nó do início. É rápido.
- **Entrar na fila:** vai para o fim se ninguém tem nível maior; vai para o início se for mais urgente que todos; senão anda até o ponto certo no meio.
- **Cancelar:** religa o nó anterior ao seguinte.

## API

| Método | Rota | O que faz |
|---|---|---|
| GET | `/api/fila` | Fila na ordem de chamada, contadores e quem foi chamado por último |
| POST | `/api/fila` | Entra na fila (`nome`, `prioridade`: NORMAL, PRIORITARIO ou EMERGENCIA) |
| POST | `/api/fila/chamar` | Chama o próximo |
| DELETE | `/api/fila/{id}` | Tira da fila quem desistiu |
