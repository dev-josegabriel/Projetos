package br.com.josedev.tarefas.estrutura;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Lista simplesmente encadeada feita do zero (sem usar java.util.LinkedList).
 *
 * Ideia: cada nó aponta para o próximo. A lista só guarda duas "pontas":
 *   inicio -> [A] -> [B] -> [C] -> null
 *                            ^ fim
 *
 * Complexidade:
 *  - adicionar no início ........... O(1)
 *  - adicionar no fim .............. O(1)  (porque guardamos o "fim")
 *  - inserir numa posição .......... O(n)  (precisa andar até lá)
 *  - buscar / remover .............. O(n)  (precisa andar procurando)
 */
public class ListaEncadeada<T> {

    private No<T> inicio;
    private No<T> fim;
    private int tamanho;

    public boolean estaVazia() {
        return tamanho == 0;
    }

    public int tamanho() {
        return tamanho;
    }

    /** Adiciona no começo da lista. O(1). */
    public void adicionarNoInicio(T valor) {
        No<T> novo = new No<>(valor);
        novo.proximo = inicio;   // o novo aponta para o antigo início
        inicio = novo;           // o novo vira o início
        if (fim == null) {       // se a lista estava vazia, ele também é o fim
            fim = novo;
        }
        tamanho++;
    }

    /** Adiciona no final da lista. O(1). */
    public void adicionarNoFim(T valor) {
        No<T> novo = new No<>(valor);
        if (estaVazia()) {
            inicio = novo;
            fim = novo;
        } else {
            fim.proximo = novo;  // o antigo fim aponta para o novo
            fim = novo;          // o novo vira o fim
        }
        tamanho++;
    }

    /**
     * Insere numa posição (começando em 0). O(n).
     * Posição 0 = início, posição igual ao tamanho = fim.
     */
    public void inserirNaPosicao(int posicao, T valor) {
        if (posicao < 0 || posicao > tamanho) {
            throw new IndexOutOfBoundsException("Posição " + posicao + " fora do intervalo 0.." + tamanho);
        }
        if (posicao == 0) {
            adicionarNoInicio(valor);
            return;
        }
        if (posicao == tamanho) {
            adicionarNoFim(valor);
            return;
        }

        // caso do meio: anda até o nó que vai ficar ANTES do novo
        No<T> anterior = inicio;
        for (int i = 0; i < posicao - 1; i++) {
            anterior = anterior.proximo;
        }

        No<T> novo = new No<>(valor);
        novo.proximo = anterior.proximo;  // o novo aponta para quem estava depois
        anterior.proximo = novo;          // o anterior passa a apontar para o novo
        tamanho++;
    }

    /** Procura o primeiro valor que satisfaz a condição. O(n). Retorna null se não achar. */
    public T buscar(Predicate<T> condicao) {
        No<T> atual = inicio;
        while (atual != null) {
            if (condicao.test(atual.valor)) {
                return atual.valor;
            }
            atual = atual.proximo;
        }
        return null;
    }

    /** Remove o primeiro valor que satisfaz a condição. O(n). Retorna true se removeu. */
    public boolean remover(Predicate<T> condicao) {
        No<T> anterior = null;
        No<T> atual = inicio;

        while (atual != null) {
            if (condicao.test(atual.valor)) {
                desligar(anterior, atual);
                return true;
            }
            anterior = atual;
            atual = atual.proximo;
        }
        return false;
    }

    /** Remove TODOS os valores que satisfazem a condição, numa única passada. O(n). */
    public int removerTodos(Predicate<T> condicao) {
        int removidos = 0;
        No<T> anterior = null;
        No<T> atual = inicio;

        while (atual != null) {
            No<T> proximo = atual.proximo;   // guarda o próximo ANTES de desligar
            if (condicao.test(atual.valor)) {
                desligar(anterior, atual);
                removidos++;
            } else {
                anterior = atual;            // só avança o "anterior" se não removeu
            }
            atual = proximo;
        }
        return removidos;
    }

    /** Percorre do início ao fim e devolve os valores numa List comum. O(n). */
    public List<T> paraLista() {
        List<T> resultado = new ArrayList<>();
        No<T> atual = inicio;
        while (atual != null) {
            resultado.add(atual.valor);
            atual = atual.proximo;
        }
        return resultado;
    }

    /** Tira um nó da lista religando o anterior ao próximo. */
    private void desligar(No<T> anterior, No<T> no) {
        if (anterior == null) {
            inicio = no.proximo;          // estava removendo o início
        } else {
            anterior.proximo = no.proximo; // o anterior "pula" o nó removido
        }
        if (no == fim) {
            fim = anterior;               // estava removendo o fim
        }
        no.proximo = null;
        tamanho--;
    }
}
