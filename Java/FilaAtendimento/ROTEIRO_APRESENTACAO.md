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
- Front-end: site com 4 páginas (Painel, Fila de espera, Histórico e Regras), em HTML, CSS e JavaScript.
- Back-end: Java 17 + Spring Boot, API REST.
- Camadas: Controller (recebe o pedido) → Service (regras e a lista de pessoas).
- Os dados ficam em memória, sem banco.

## 4. Demonstração ao vivo (2 min)
1. Rodar: `mvn spring-boot:run` e abrir http://localhost:8080
2. O site começa vazio. Mostrar o menu: Painel, Fila de espera, Histórico e Regras.
3. Na **Fila de espera**, cadastrar na hora: Maria (Normal), João (Normal), Helena (Prioritário) e Carlos (Emergência), nessa ordem.
4. Mostrar que Maria e João chegaram primeiro, mas Carlos e Helena vêm na frente.
5. Cadastrar outra **Emergência**: passa na frente de todos, menos de outra emergência anterior.
6. Clicar em **Chamar próximo**: aparece a senha em destaque.
7. Tentar o mesmo nome duas vezes: aparece o erro "Essa pessoa já está na fila."
8. Ir ao **Painel** (contadores e gráfico), ao **Histórico** (tempo de espera) e às **Regras**.

## 5. Possíveis perguntas
- **Por que não usou fila comum?** Porque ela chama só por ordem de chegada, e aqui a urgência importa.
- **E se duas pessoas tiverem a mesma prioridade?** Ganha quem chegou antes (menor id).
- **Os dados somem?** Sim, ficam em memória. Num sistema real entraria um banco de dados.
- **Onde fica a regra de prioridade?** No `FilaService`, no Comparator que ordena a lista.
