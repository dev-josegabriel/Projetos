# Roteiro de apresentação: Fila de Atendimento com Prioridade

Tempo: uns 5 minutos. Fala do jeito que você fala, é só pra ter o caminho.

## 1. O problema (1 min)
- Numa clínica ou UPA, quem chega primeiro nem sempre deve ser atendido primeiro.
- Uma emergência passa na frente de um prioritário, que passa na frente de um atendimento normal.
- Dentro do mesmo nível, vale a ordem de chegada.
- Uma fila comum (primeiro que entra, primeiro que sai) não resolve isso.

## 2. A regra de prioridade (1 min)
- Três níveis: Emergência (1), Prioritário (2) e Normal (3).
- A fila é ordenada por nível e, no empate, por quem chegou antes.
- Cada pessoa recebe uma senha: a primeira letra mostra o nível (E004, P003, N001).

## 3. Como o sistema está montado (1 min)
- Front-end: HTML, CSS e JavaScript (tela única).
- Back-end: Java 17 + Spring Boot, API REST.
- Camadas: Controller (recebe o pedido) → Service (regras e a lista de pessoas).
- Os dados ficam em memória, sem banco.

## 4. Demonstração ao vivo (2 min)
1. Rodar: `mvn spring-boot:run` e abrir http://localhost:8080
2. Mostrar a fila inicial: Maria e João chegaram primeiro, mas Carlos (emergência) e Helena (prioritária) vêm na frente.
3. Adicionar uma pessoa **Normal**: vai pro fim.
4. Adicionar uma **Emergência**: passa na frente de todos, menos de outra emergência anterior.
5. Clicar em **Chamar próximo**: aparece a senha em destaque e a pessoa vai pros "Últimos chamados".
6. Tentar o mesmo nome duas vezes: aparece o erro "Essa pessoa já está na fila."
7. Cancelar alguém pelo X.

## 5. Possíveis perguntas
- **Por que não usou fila comum?** Porque ela chama só por ordem de chegada, e aqui a urgência importa.
- **E se duas pessoas tiverem a mesma prioridade?** Ganha quem chegou antes (menor id).
- **Os dados somem?** Sim, ficam em memória. Num sistema real entraria um banco de dados.
- **Onde fica a regra de prioridade?** No `FilaService`, no Comparator que ordena a lista.
