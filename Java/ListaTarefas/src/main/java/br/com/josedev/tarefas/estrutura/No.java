package br.com.josedev.tarefas.estrutura;

/**
 * Um "nó" da lista encadeada.
 * Cada nó guarda um valor e aponta para o próximo nó (ou para null, se for o último).
 */
public class No<T> {

    final T valor;
    No<T> proximo;

    No(T valor) {
        this.valor = valor;
    }
}
