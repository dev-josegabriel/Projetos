# # 🏥 Fila de Atendimento com Prioridade

Sistema de terminal em Python que simula a **triagem de uma UPA/UBS**: os pacientes mais graves são atendidos primeiro e, em caso de empate, vale a ordem de chegada.

---

## 📌 Problema real

Em um pronto atendimento, uma fila comum (FIFO) faz um caso grave esperar atrás de casos leves só porque chegou depois. Este sistema resolve isso organizando a fila por **nível de gravidade**, garantindo justiça entre pacientes do mesmo nível.

| Nível | Classificação  |
|:-----:|----------------|
| 1     | Emergência     |
| 2     | Muito urgente  |
| 3     | Urgente        |
| 4     | Pouco urgente  |
| 5     | Não urgente    |

---

## ✨ Funcionalidades

- Registrar paciente (gera senha automática: `P001`, `P002`, ...)
- Chamar o próximo da fila
- Ver quem é o próximo sem removê-lo
- Listar a fila completa na ordem de atendimento
- Reclassificar a prioridade de um paciente (ex.: o quadro piorou)
- Cancelar uma senha
- Histórico de atendidos e tempo médio de espera por nível
- Persistência automática em `dados.json` (a fila sobrevive ao fechar o programa)

---

## 🧠 Estrutura de dados: Min-Heap

A fila é uma **Min-Heap implementada do zero** (sem `heapq`) em `estruturas/heap.py`.

Uma heap é uma árvore binária armazenada em um array, onde **todo pai é menor ou igual aos filhos** — assim, o menor elemento (o paciente mais prioritário) fica sempre na raiz.

Para um elemento na posição `i` do array:

- pai: `(i - 1) // 2`
- filho esquerdo: `2 * i + 1`
- filho direito: `2 * i + 2`

Os dois movimentos que mantêm a regra:

- **`_subir`**: após inserir no fim do array, troca com o pai enquanto for menor que ele.
- **`_descer`**: após remover a raiz (substituída pelo último elemento), troca com o menor filho enquanto for maior que ele.

**Critério de ordenação** (`Paciente.__lt__`): `(prioridade, ordem_de_chegada)`.

### Complexidade

| Operação                     | Complexidade |
|------------------------------|:------------:|
| Registrar paciente           | O(log n)     |
| Ver próximo                  | O(1)         |
| Chamar próximo               | O(log n)     |
| Cancelar / reclassificar     | O(n)         |
| Listar em ordem              | O(n log n)   |

> Cancelar e reclassificar são O(n) porque é preciso localizar a senha no array (busca linear); a reorganização em si é O(log n).

---

## ▶️ Como executar

Requisitos: **Python 3.8+** (nenhuma biblioteca externa).

```bash
python main.py
```

### Exemplo de uso

```
========== FILA DE ATENDIMENTO COM PRIORIDADE ==========
 1) Registrar paciente
 2) Chamar próximo
 3) Ver próximo da fila
 4) Listar fila (em ordem de atendimento)
 5) Reclassificar prioridade
 6) Cancelar senha
 7) Histórico e estatísticas
 0) Salvar e sair
========================================================
Escolha: 1
Nome do paciente: Ana
Nível (1-5): 4
✔ Senha P001 gerada para Ana (Pouco urgente).
```

Se a Ana (nível 4) chegou primeiro, mas depois o Davi (nível 1) é registrado, **o Davi é chamado antes**.

---

## 🧪 Testes

```bash
python -m unittest discover -s testes -v
```

Cobrem: ordenação da heap, remoção no meio, fila vazia, desempate por chegada, reclassificação, cancelamento, validações e salvar/carregar.

---

## 📁 Estrutura do projeto

```
fila_prioritaria/
├── main.py              # menu de terminal
├── modelos.py           # Paciente e níveis de prioridade
├── servico.py           # regras de negócio (FilaAtendimento)
├── persistencia.py      # salvar/carregar em dados.json
├── estruturas/
│   └── heap.py          # Min-Heap feita à mão
├── testes/
│   └── test_fila.py     # testes unitários
└── README.md
```

---

## 🚀 Ideias para evoluir

- Interface web ou API REST (Flask/FastAPI)
- Múltiplos guichês/médicos chamando pacientes em paralelo
- Prioridade que aumenta com o tempo de espera (evita "starvation" de casos leves)
- Trocar o JSON por um banco de dados (SQLite)
