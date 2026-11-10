# ✅ To_Do_List — Full Stack

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-brightgreen?style=for-the-badge&logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-blue?style=for-the-badge&logo=postgresql)
![Maven](https://img.shields.io/badge/Maven-red?style=for-the-badge&logo=apachemaven)
![JavaScript](https://img.shields.io/badge/JavaScript-yellow?style=for-the-badge&logo=javascript)
![Bootstrap](https://img.shields.io/badge/Bootstrap-5-purple?style=for-the-badge&logo=bootstrap)
![Status](https://img.shields.io/badge/Status-Concluído-success?style=for-the-badge)

> Aplicação Full Stack de gerenciamento de tarefas desenvolvida com Spring Boot, PostgreSQL e frontend em HTML/CSS/JS puro. Possui interface visual futurística com tema neon, kanban board, relatórios e configurações dinâmicas.

---

## 🎨 Preview

> Dashboard com kanban board separado por status (Pendente, Em Andamento, Concluída), cards de estatísticas, tabela de tarefas, relatórios visuais e configurações de aparência.

---

## 📋 Funcionalidades

### Backend
- ✅ Criar tarefas com validação de título e prioridade
- ✅ Listar todas as tarefas
- ✅ Atualizar tarefas por ID
- ✅ Deletar tarefas por ID
- ✅ Status controlado por Enum (`PENDENTE`, `EM_ANDAMENTO`, `CONCLUIDA`)
- ✅ CORS configurado para integração com o frontend
- ✅ Tratamento de erros com mensagens descritivas

### Frontend
- ✅ Dashboard com Kanban Board (3 colunas por status)
- ✅ Cards de estatísticas em tempo real
- ✅ Tabela completa com filtros por status e prioridade
- ✅ Relatórios visuais com gráficos de barras
- ✅ Modal de criação e edição de tarefas
- ✅ Modal de confirmação de exclusão
- ✅ Busca em tempo real
- ✅ Configurações de aparência (cor de destaque, animações, scanline)
- ✅ Tema futurístico neon green on black

---

## 🏗️ Estrutura do Projeto

```
src/
└── main/
    ├── java/
    │   └── br/com/josedev/to_do_list/
    │       ├── config/
    │       │   └── CorsConfig.java
    │       ├── controller/
    │       │   └── TaskController.java
    │       ├── model/
    │       │   ├── Task.java
    │       │   └── TaskStatus.java
    │       ├── repository/
    │       │   └── TaskRepository.java
    │       └── service/
    │           └── TaskService.java
    └── resources/
        ├── static/
        │   ├── index.html
        │   ├── styles.css
        │   └── scripts.js
        └── application.properties
```

---

## 🗄️ Entidade Task

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | Long | Identificador único (gerado automaticamente) |
| `title` | String | Título da tarefa (obrigatório) |
| `description` | String | Descrição detalhada da tarefa |
| `status` | TaskStatus (Enum) | `PENDENTE`, `EM_ANDAMENTO` ou `CONCLUIDA` |
| `priority` | Integer | Prioridade de 1 (baixa) a 5 (alta) |

### TaskStatus Enum

```java
public enum TaskStatus {
    PENDENTE("Pendente"),
    EM_ANDAMENTO("Em Andamento"),
    CONCLUIDA("Concluída");
}
```

---

## 🔀 Endpoints

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/tasks` | Cria uma nova tarefa |
| `GET` | `/tasks` | Lista todas as tarefas |
| `PUT` | `/tasks/{id}` | Atualiza uma tarefa pelo ID |
| `DELETE` | `/tasks/{id}` | Deleta uma tarefa pelo ID |

---

## 📦 Exemplo de Requisição

### POST /tasks
```json
{
  "title": "Estudar Spring Boot",
  "description": "Revisar camada de serviço e controller",
  "status": "EM_ANDAMENTO",
  "priority": 5
}
```

### Resposta (201 Created)
```json
{
  "id": 1,
  "title": "Estudar Spring Boot",
  "description": "Revisar camada de serviço e controller",
  "status": "EM_ANDAMENTO",
  "priority": 5
}
```

---

## ⚙️ Como Rodar o Projeto

### Pré-requisitos
- Java 21
- Maven
- PostgreSQL

### 1. Clone o repositório
```bash
git clone https://github.com/josegabriel2006/To_Do_List.git
cd To_Do_List
```

### 2. Configure o banco de dados
Crie um banco no PostgreSQL:
```sql
CREATE DATABASE To_Do_List;
```

### 3. Configure o `application.properties`
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/To_Do_List
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
```

### 4. Execute o projeto
```bash
mvn spring-boot:run
```

### 5. Acesse o frontend
Abra o navegador em:
```
http://localhost:8080
```

A API estará disponível em:
```
http://localhost:8080/tasks
```

---

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java 21**
- **Spring Boot 4.0.6**
- **Spring Web** — criação dos endpoints REST
- **Spring Data JPA** — camada de persistência
- **PostgreSQL** — banco de dados relacional
- **Maven** — gerenciamento de dependências

### Frontend
- **HTML5 / CSS3 / JavaScript** — estrutura e lógica da interface
- **Bootstrap 5** — componentes e grid system
- **Bootstrap Icons** — ícones
- **Google Fonts** — fontes Rajdhani e Share Tech Mono

---

## 👨‍💻 Autor

Feito por **José Gabriel** — estudante de Engenharia de Software e futuro desenvolvedor Back-end Java.

[![GitHub](https://img.shields.io/badge/GitHub-josegabriel2006-black?style=flat-square&logo=github)](https://github.com/josegabriel2006)
