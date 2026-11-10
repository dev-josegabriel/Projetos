package br.com.josedev.tarefas;

import br.com.josedev.tarefas.estrutura.ListaEncadeada;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListaEncadeadaTest {

    @Test
    void listaNovaEstaVazia() {
        ListaEncadeada<String> lista = new ListaEncadeada<>();
        assertTrue(lista.estaVazia());
        assertEquals(0, lista.tamanho());
        assertEquals(List.of(), lista.paraLista());
    }

    @Test
    void adicionaNoInicioENoFim() {
        ListaEncadeada<String> lista = new ListaEncadeada<>();
        lista.adicionarNoFim("B");
        lista.adicionarNoInicio("A");
        lista.adicionarNoFim("C");

        assertEquals(List.of("A", "B", "C"), lista.paraLista());
        assertEquals(3, lista.tamanho());
    }

    @Test
    void insereNaPosicaoDoMeio() {
        ListaEncadeada<String> lista = new ListaEncadeada<>();
        lista.adicionarNoFim("A");
        lista.adicionarNoFim("C");

        lista.inserirNaPosicao(1, "B");

        assertEquals(List.of("A", "B", "C"), lista.paraLista());
    }

    @Test
    void inserirNasPontasPelaPosicao() {
        ListaEncadeada<String> lista = new ListaEncadeada<>();
        lista.inserirNaPosicao(0, "B");   // lista vazia
        lista.inserirNaPosicao(0, "A");   // início
        lista.inserirNaPosicao(2, "C");   // fim

        assertEquals(List.of("A", "B", "C"), lista.paraLista());
    }

    @Test
    void posicaoInvalidaLancaExcecao() {
        ListaEncadeada<String> lista = new ListaEncadeada<>();
        lista.adicionarNoFim("A");

        assertThrows(IndexOutOfBoundsException.class, () -> lista.inserirNaPosicao(-1, "X"));
        assertThrows(IndexOutOfBoundsException.class, () -> lista.inserirNaPosicao(2, "X"));
    }

    @Test
    void removeInicioMeioEFim() {
        ListaEncadeada<String> lista = new ListaEncadeada<>();
        for (String s : List.of("A", "B", "C", "D")) {
            lista.adicionarNoFim(s);
        }

        assertTrue(lista.remover(s -> s.equals("A")));   // início
        assertTrue(lista.remover(s -> s.equals("C")));   // meio
        assertTrue(lista.remover(s -> s.equals("D")));   // fim
        assertEquals(List.of("B"), lista.paraLista());

        assertFalse(lista.remover(s -> s.equals("Z")));  // não existe
    }

    @Test
    void depoisDeRemoverOFimAindaDaPraAdicionarNoFim() {
        ListaEncadeada<String> lista = new ListaEncadeada<>();
        lista.adicionarNoFim("A");
        lista.adicionarNoFim("B");
        lista.remover(s -> s.equals("B"));   // "fim" tem que voltar a apontar pro A

        lista.adicionarNoFim("C");

        assertEquals(List.of("A", "C"), lista.paraLista());
    }

    @Test
    void removerTudoDeixaAListaVaziaEUtilizavel() {
        ListaEncadeada<String> lista = new ListaEncadeada<>();
        lista.adicionarNoFim("A");
        lista.remover(s -> true);

        assertTrue(lista.estaVazia());
        lista.adicionarNoFim("B");
        assertEquals(List.of("B"), lista.paraLista());
    }

    @Test
    void removerTodosTiraVariosEmUmaPassada() {
        ListaEncadeada<Integer> lista = new ListaEncadeada<>();
        for (int i = 1; i <= 8; i++) {
            lista.adicionarNoFim(i);
        }

        int removidos = lista.removerTodos(n -> n % 2 == 0);

        assertEquals(4, removidos);
        assertEquals(List.of(1, 3, 5, 7), lista.paraLista());
        assertEquals(4, lista.tamanho());
    }

    @Test
    void removerTodosFuncionaQuandoTudoSeraRemovido() {
        ListaEncadeada<Integer> lista = new ListaEncadeada<>();
        lista.adicionarNoFim(1);
        lista.adicionarNoFim(2);

        assertEquals(2, lista.removerTodos(n -> true));
        assertTrue(lista.estaVazia());
    }

    @Test
    void buscaDevolveOValorOuNull() {
        ListaEncadeada<String> lista = new ListaEncadeada<>();
        lista.adicionarNoFim("A");
        lista.adicionarNoFim("B");

        assertEquals("B", lista.buscar(s -> s.equals("B")));
        assertNull(lista.buscar(s -> s.equals("Z")));
    }
}
