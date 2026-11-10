package br.com.josedev.fila.estrutura;

import br.com.josedev.fila.model.Atendimento;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Fila com prioridade implementada com LISTA ENCADEADA (sem usar PriorityQueue nem LinkedList do Java).
 *
 * A lista é mantida SEMPRE ORDENADA: quem tem prioridade menor (nível 1 = emergência) fica na frente.
 * Entre pessoas com a mesma prioridade vale a ordem de chegada (primeiro a chegar, primeiro a sair).
 * Por isso, "chamar o próximo" é só tirar o nó do início.
 */
public class FilaPrioritaria {

    private static final class No {
        final Atendimento valor;
        No proximo;

        No(Atendimento valor) {
            this.valor = valor;
        }
    }

    private No inicio;
    private No fim;
    private int tamanho;

    /** Coloca a pessoa na posição certa: depois de todos com prioridade igual ou mais urgente. */
    public void enfileirar(Atendimento atendimento) {
        No novo = new No(atendimento);
        int nivel = atendimento.prioridade().getNivel();

        // Caso comum e rápido: fila vazia, ou ninguém com nível maior -> entra no fim.
        if (inicio == null) {
            inicio = novo;
            fim = novo;
        } else if (fim.valor.prioridade().getNivel() <= nivel) {
            fim.proximo = novo;
            fim = novo;
        } else if (inicio.valor.prioridade().getNivel() > nivel) {
            // Mais urgente que todo mundo -> vira o primeiro.
            novo.proximo = inicio;
            inicio = novo;
        } else {
            // Caso do meio: anda até o último nó com prioridade igual ou mais urgente.
            No atual = inicio;
            while (atual.proximo != null && atual.proximo.valor.prioridade().getNivel() <= nivel) {
                atual = atual.proximo;
            }
            novo.proximo = atual.proximo;
            atual.proximo = novo;
        }
        tamanho++;
    }

    /** Tira e devolve quem está no início (o próximo a ser chamado). Devolve null se a fila está vazia. */
    public Atendimento desenfileirar() {
        if (inicio == null) {
            return null;
        }
        Atendimento chamado = inicio.valor;
        inicio = inicio.proximo;
        if (inicio == null) {
            fim = null;
        }
        tamanho--;
        return chamado;
    }

    /** Olha quem é o próximo, sem tirar da fila. */
    public Atendimento primeiro() {
        return inicio == null ? null : inicio.valor;
    }

    public Atendimento buscar(Predicate<Atendimento> criterio) {
        for (No atual = inicio; atual != null; atual = atual.proximo) {
            if (criterio.test(atual.valor)) {
                return atual.valor;
            }
        }
        return null;
    }

    /** Remove o primeiro que combinar com o critério (ex.: pessoa que desistiu). Devolve true se removeu. */
    public boolean remover(Predicate<Atendimento> criterio) {
        No anterior = null;
        for (No atual = inicio; atual != null; anterior = atual, atual = atual.proximo) {
            if (criterio.test(atual.valor)) {
                if (anterior == null) {
                    inicio = atual.proximo;
                } else {
                    anterior.proximo = atual.proximo;
                }
                if (atual == fim) {
                    fim = anterior;
                }
                tamanho--;
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

    /** Cópia da fila em ordem de atendimento, para mostrar na tela. */
    public List<Atendimento> paraLista() {
        List<Atendimento> lista = new ArrayList<>();
        for (No atual = inicio; atual != null; atual = atual.proximo) {
            lista.add(atual.valor);
        }
        return lista;
    }
}
