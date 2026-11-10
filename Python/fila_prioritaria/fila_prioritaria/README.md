# Fila de Atendimento com Prioridade (Python)

Sistema de terminal que simula a triagem de uma UPA/UBS: pacientes mais graves
são atendidos primeiro; em caso de empate, vale a ordem de chegada.

## Problema real
Em um pronto atendimento, uma fila comum (FIFO) faz um caso grave esperar atrás
de casos leves. Aqui a fila é organizada por **nível de gravidade**.

## Estrutura de dados: Min-Heap
Implementada do zero em `estruturas/heap.py`, guardada num array.

| Operação                    | Complexidade |
|-----------------------------|--------------|
| Registrar paciente          | O(log n)     |
| Ver próximo                 | O(1)         |
| Chamar próximo              | O(log n)     |
| Cancelar / reclassificar    | O(n)         |
| Listar em ordem             | O(n log n)   |

Critério de ordenação (`Paciente.__lt__`): `(prioridade, ordem_de_chegada)`.

## Como rodar
```bash
python main.py
```
Requer Python 3.8+ e nenhuma biblioteca externa.

## Testes
```bash
python -m unittest discover -s testes -v
```

## Estrutura
```
main.py            # menu de terminal
modelos.py         # Paciente e níveis de prioridade
servico.py         # regras de negócio (FilaAtendimento)
persistencia.py    # salvar/carregar em dados.json
estruturas/heap.py # Min-Heap feita à mão
testes/            # testes unitários
```
