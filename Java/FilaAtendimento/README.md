# Fila de Atendimento com Prioridade

Sistema de triagem para a recepção de uma clínica: **emergências** são chamadas antes dos **prioritários** (idosos, gestantes, PCD), e estes antes dos **normais**. Quem tem a mesma prioridade é chamado por ordem de chegada.

API REST em **Java + Spring Boot** com um site de 4 páginas em HTML/CSS/JS (Painel, Fila de espera, Histórico e Regras). As pessoas ficam guardadas em uma lista na memória do servidor e a fila é mostrada sempre na ordem certa de chamada.

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

`model`, `dto`, `service`, `controller`, `exception`. O site fica em `resources/static` (`index.html`, `fila.html`, `historico.html`, `regras.html`, mais `css/` e `js/`).

### 4) Banco

Não há. Os dados ficam em memória (se o servidor reiniciar, o sistema volta vazio).

### 5) Entidade

**Atendimento:** id, senha, nome, prioridade, chegada.

**Chamado:** id, senha, nome, prioridade, chegada, chamadoEm.

### 6) Relacionamentos

Nenhum. Cada pessoa é um item independente na lista.

### 7) Regras de negócio

- Nome obrigatório (até 60 caracteres) e prioridade obrigatória.
- Emergência (1) passa na frente de prioritário (2), que passa na frente de normal (3).
- Mesma prioridade: ordem de chegada.
- A mesma pessoa não entra duas vezes (ignora maiúsculas).
- Chamar o próximo com a fila vazia dá erro 409; cancelar quem não está na fila dá 404.

## Páginas do site

| Página | O que mostra |
|---|---|
| Painel (`index.html`) | Contadores, próximos da fila, gráfico por prioridade, últimos chamados e botão de chamar |
| Fila de espera (`fila.html`) | Cadastro de chegada, fila completa e cancelamento |
| Histórico (`historico.html`) | Tabela de quem já foi chamado e tempo de espera |
| Regras (`regras.html`) | Como a prioridade funciona, exemplo e tecnologias |

## Regra de prioridade

A fila é montada pelo `FilaService`, que guarda as pessoas em uma lista e a ordena com um `Comparator`:

1. **Emergência**, depois **Prioritário**, depois **Normal**.
2. No mesmo nível, quem chegou antes (menor id) é chamado antes.

- **Entrar na fila:** a pessoa é adicionada e a fila já aparece na ordem certa.
- **Chamar o próximo:** sai o primeiro da fila ordenada e vai para o histórico, com o horário da chamada.
- **Cancelar:** a pessoa é removida da lista.

## API

| Método | Rota | O que faz |
|---|---|---|
| GET | `/api/fila` | Fila na ordem de chamada, contadores e quem foi chamado por último |
| POST | `/api/fila` | Entra na fila (`nome`, `prioridade`: NORMAL, PRIORITARIO ou EMERGENCIA) |
| POST | `/api/fila/chamar` | Chama o próximo |
| DELETE | `/api/fila/{id}` | Tira da fila quem desistiu |

## Publicar na internet (Render)

O projeto já tem `Dockerfile` e `render.yaml`. Passo a passo:

1. Suba o código no GitHub (esta pasta precisa estar na **raiz** de um repositório, ou use o campo *Root Directory* apontando para `Java/FilaAtendimento`).
2. No [Render](https://render.com), clique em **New +** → **Web Service** e conecte o repositório.
3. Em *Language*, escolha **Docker**. Em *Instance Type*, **Free**. Clique em **Deploy**.
4. Quando terminar (uns 3 a 5 minutos), o Render mostra o link público `https://fila-atendimento-xxxx.onrender.com`.

No plano grátis o serviço "dorme" depois de um tempo parado; o primeiro acesso demora uns 30 segundos.

## Versão online (GitHub Pages)

A pasta `docs/fila-atendimento` do repositório tem uma cópia do site. Sem o servidor Java, o site entra em **modo demonstração**: as mesmas regras rodam em JavaScript e os dados ficam salvos no navegador. Link: https://dev-josegabriel.github.io/Projetos/fila-atendimento/
