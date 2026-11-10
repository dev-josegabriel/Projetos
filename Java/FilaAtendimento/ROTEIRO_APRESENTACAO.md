# Roteiro de apresentação: Fila de Atendimento com Prioridade

Tempo: uns 5 a 7 minutos. Fala do jeito que você fala, é só pra ter o caminho.

## 1. O problema (1 min)
- Numa clínica ou UPA, quem chega primeiro nem sempre deve ser atendido primeiro.
- Uma emergência passa na frente de um prioritário, que passa na frente de um atendimento normal.
- Dentro do mesmo nível, vale a ordem de chegada (quem chegou antes é chamado antes).
- Fila comum (primeiro que entra, primeiro que sai) não resolve isso.

## 2. A estrutura de dados: heap (2 min)
- É uma árvore guardada dentro de um vetor.
- Regra: cada pessoa é mais urgente que as duas filhas dela. Por isso a **raiz é sempre o próximo a ser chamado**.
- Pai de `i` = `(i - 1) / 2`. Filhos de `i` = `2i + 1` e `2i + 2`.
- Entrou alguém: vai pro fim do vetor e **sobe** enquanto for mais urgente que o pai.
- Chamou alguém: tira a raiz, coloca o último no lugar e **desce**, trocando com o filho mais urgente.
- Desempate: mesmo nível de prioridade, quem tem o menor id (chegou antes) ganha.
- Por que é bom: ver quem é o próximo é instantâneo, e entrar ou chamar mexe em poucas posições, mesmo com fila grande.
- Fiz o heap na mão, sem `PriorityQueue` do Java.

## 3. Como o sistema está montado (1 min)
- Front-end: HTML, CSS e JavaScript (tela única).
- Back-end: Java 17 + Spring Boot, API REST.
- Camadas: Controller (recebe o pedido) → Service (regras) → FilaPrioritaria (o heap).
- Os dados ficam em memória, sem banco.

## 4. Demonstração ao vivo (2 min)
1. Rodar: `mvn spring-boot:run` e abrir http://localhost:8080
2. Mostrar a fila inicial e, no desenho do heap, quem está na raiz.
3. Adicionar uma pessoa **Normal**: entra no fim da fila.
4. Adicionar uma **Emergência**: passa pra frente e a árvore se reorganiza (mostrar a raiz mudando).
5. Clicar em **Chamar próximo**: sai a raiz e o heap se reorganiza.
6. Tentar o mesmo nome duas vezes: aparece o erro "Essa pessoa já está na fila."
7. Cancelar alguém do meio da fila.

## 5. Possíveis perguntas
- **Por que heap e não uma lista ordenada?** Na lista ordenada, cada pessoa nova obriga a procurar o lugar dela e deslocar vários itens. No heap só sobe ou desce alguns níveis.
- **E se duas pessoas tiverem a mesma prioridade?** Ganha quem chegou antes (menor id).
- **Por que não usou `PriorityQueue`?** Pra mostrar que entendo como a estrutura funciona por dentro.
- **E se o vetor encher?** Ele dobra de tamanho.
- **Os dados somem?** Sim, ficam em memória. Num sistema real entraria um banco de dados.
- **Como cancela alguém do meio?** O último do vetor ocupa o lugar e é reorganizado (sobe ou desce).
