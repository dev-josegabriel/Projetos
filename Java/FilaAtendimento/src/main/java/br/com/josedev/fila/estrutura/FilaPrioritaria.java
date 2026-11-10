package br.com.josedev.fila.estrutura;

import br.com.josedev.fila.model.Atendimento;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Fila com prioridade implementada com um HEAP MÍNIMO (árvore binária guardada em um vetor).
 * Não usa PriorityQueue do Java.
 *
 * Regra do heap: cada pessoa é "mais urgente" que as duas pessoas filhas dela. Por isso a RAIZ (posição 0)
 * é sempre o próximo a ser chamado.
 *
 * Quem é mais urgente? Menor nível de prioridade (1 = emergência). Se o nível for igual, quem chegou antes
 * (menor id) vem primeiro.
 *
 * Posições no vetor: o pai de i fica em (i - 1) / 2; os filhos ficam em 2i + 1 e 2i + 2.
 */
public class FilaPrioritaria {

    private Atendimento[] heap = new Atendimento[8];
    private int tamanho = 0;

    /** Entra na fila: vai para o fim do vetor e "sobe" enquanto for mais urgente que o pai. O(log n). */
    public void enfileirar(Atendimento atendimento) {
        if (tamanho == heap.length) {
            Atendimento[] maior = new Atendimento[heap.length * 2];
            System.arraycopy(heap, 0, maior, 0, tamanho);
            heap = maior;
        }
        heap[tamanho] = atendimento;
        subir(tamanho);
        tamanho++;
    }

    /** Chama o próximo: tira a raiz, põe o último no lugar dela e "desce" até a posição certa. O(log n). */
    public Atendimento desenfileirar() {
        if (tamanho == 0) {
            return null;
        }
        Atendimento chamado = heap[0];
        tamanho--;
        heap[0] = heap[tamanho];
        heap[tamanho] = null;
        if (tamanho > 0) {
            descer(0);
        }
        return chamado;
    }

    /** Olha quem é o próximo, sem tirar da fila. */
    public Atendimento primeiro() {
        return tamanho == 0 ? null : heap[0];
    }

    public Atendimento buscar(Predicate<Atendimento> criterio) {
        for (int i = 0; i < tamanho; i++) {
            if (criterio.test(heap[i])) {
                return heap[i];
            }
        }
        return null;
    }

    /** Tira quem desistiu: o último ocupa o lugar dele e depois é reorganizado. Devolve true se removeu. */
    public boolean remover(Predicate<Atendimento> criterio) {
        for (int i = 0; i < tamanho; i++) {
            if (criterio.test(heap[i])) {
                tamanho--;
                heap[i] = heap[tamanho];
                heap[tamanho] = null;
                if (i < tamanho) {
                    descer(i);
                    subir(i);
                }
                return true;
            }
        }
        return false;
    }

    public int tamanho() {
        return tamanho;
    }

    public boolean vazia() {
        return tamanho == 0;
    }

    /** O vetor do heap do jeito que está na memória (usado para desenhar a árvore na tela). */
    public List<Atendimento> paraArray() {
        List<Atendimento> lista = new ArrayList<>(tamanho);
        for (int i = 0; i < tamanho; i++) {
            lista.add(heap[i]);
        }
        return lista;
    }

    /** Fila na ordem em que as pessoas serão chamadas (trabalha numa cópia, não mexe na fila real). */
    public List<Atendimento> paraLista() {
        FilaPrioritaria copia = new FilaPrioritaria();
        copia.heap = java.util.Arrays.copyOf(heap, heap.length);
        copia.tamanho = tamanho;
        List<Atendimento> ordenada = new ArrayList<>(tamanho);
        while (!copia.vazia()) {
            ordenada.add(copia.desenfileirar());
        }
        return ordenada;
    }

    // ---------- reorganização do heap ----------

    private boolean maisUrgente(Atendimento a, Atendimento b) {
        int na = a.prioridade().getNivel();
        int nb = b.prioridade().getNivel();
        return na < nb || (na == nb && a.id() < b.id());
    }

    private void subir(int i) {
        while (i > 0) {
            int pai = (i - 1) / 2;
            if (!maisUrgente(heap[i], heap[pai])) {
                break;
            }
            trocar(i, pai);
            i = pai;
        }
    }

    private void descer(int i) {
        while (true) {
            int esquerdo = 2 * i + 1;
            int direito = 2 * i + 2;
            int menor = i;
            if (esquerdo < tamanho && maisUrgente(heap[esquerdo], heap[menor])) {
                menor = esquerdo;
            }
            if (direito < tamanho && maisUrgente(heap[direito], heap[menor])) {
                menor = direito;
            }
            if (menor == i) {
                break;
            }
            trocar(i, menor);
            i = menor;
        }
    }

    private void trocar(int i, int j) {
        Atendimento temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }
}
