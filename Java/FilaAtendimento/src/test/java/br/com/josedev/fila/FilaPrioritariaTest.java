package br.com.josedev.fila;

import br.com.josedev.fila.estrutura.FilaPrioritaria;
import br.com.josedev.fila.model.Atendimento;
import br.com.josedev.fila.model.Prioridade;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class FilaPrioritariaTest {

    private static Atendimento a(int id, String nome, Prioridade p) {
        return new Atendimento(id, "S" + id, nome, p, "10:00");
    }

    private static List<String> nomes(FilaPrioritaria fila) {
        return fila.paraLista().stream().map(Atendimento::nome).collect(Collectors.toList());
    }

    @Test
    void filaNovaEstaVazia() {
        FilaPrioritaria fila = new FilaPrioritaria();
        assertTrue(fila.vazia());
        assertNull(fila.primeiro());
        assertNull(fila.desenfileirar());
    }

    @Test
    void mesmaPrioridadeSegueOrdemDeChegada() {
        FilaPrioritaria fila = new FilaPrioritaria();
        fila.enfileirar(a(1, "Ana", Prioridade.NORMAL));
        fila.enfileirar(a(2, "Bia", Prioridade.NORMAL));
        fila.enfileirar(a(3, "Caio", Prioridade.NORMAL));
        assertEquals(List.of("Ana", "Bia", "Caio"), nomes(fila));
    }

    @Test
    void emergenciaPassaNaFrenteDeTodos() {
        FilaPrioritaria fila = new FilaPrioritaria();
        fila.enfileirar(a(1, "Ana", Prioridade.NORMAL));
        fila.enfileirar(a(2, "Bia", Prioridade.PRIORITARIO));
        fila.enfileirar(a(3, "Caio", Prioridade.EMERGENCIA));
        assertEquals(List.of("Caio", "Bia", "Ana"), nomes(fila));
    }

    @Test
    void prioritarioEntraNoMeioDepoisDeEmergenciaEAntesDeNormal() {
        FilaPrioritaria fila = new FilaPrioritaria();
        fila.enfileirar(a(1, "Ana", Prioridade.EMERGENCIA));
        fila.enfileirar(a(2, "Bia", Prioridade.NORMAL));
        fila.enfileirar(a(3, "Caio", Prioridade.PRIORITARIO));
        fila.enfileirar(a(4, "Duda", Prioridade.PRIORITARIO));
        assertEquals(List.of("Ana", "Caio", "Duda", "Bia"), nomes(fila));
    }

    @Test
    void desenfileirarChamaOPrimeiroEAtualizaOTamanho() {
        FilaPrioritaria fila = new FilaPrioritaria();
        fila.enfileirar(a(1, "Ana", Prioridade.NORMAL));
        fila.enfileirar(a(2, "Bia", Prioridade.EMERGENCIA));
        assertEquals("Bia", fila.desenfileirar().nome());
        assertEquals(1, fila.tamanho());
        assertEquals("Ana", fila.desenfileirar().nome());
        assertTrue(fila.vazia());
    }

    @Test
    void depoisDeEsvaziarAindaDaParaUsar() {
        FilaPrioritaria fila = new FilaPrioritaria();
        fila.enfileirar(a(1, "Ana", Prioridade.NORMAL));
        fila.desenfileirar();
        fila.enfileirar(a(2, "Bia", Prioridade.NORMAL));
        fila.enfileirar(a(3, "Caio", Prioridade.NORMAL));
        assertEquals(List.of("Bia", "Caio"), nomes(fila));
    }

    @Test
    void aRaizDoHeapEhSempreOProximo() {
        FilaPrioritaria fila = new FilaPrioritaria();
        fila.enfileirar(a(1, "Ana", Prioridade.NORMAL));
        fila.enfileirar(a(2, "Bia", Prioridade.PRIORITARIO));
        fila.enfileirar(a(3, "Caio", Prioridade.EMERGENCIA));
        assertEquals("Caio", fila.primeiro().nome());
        assertEquals("Caio", fila.paraArray().get(0).nome());
    }

    @Test
    void cresceAlemDaCapacidadeInicial() {
        FilaPrioritaria fila = new FilaPrioritaria();
        for (int i = 1; i <= 30; i++) {
            fila.enfileirar(a(i, "P" + i, i % 3 == 0 ? Prioridade.EMERGENCIA : Prioridade.NORMAL));
        }
        assertEquals(30, fila.tamanho());
        int anterior = 0;
        while (!fila.vazia()) {
            int nivel = fila.desenfileirar().prioridade().getNivel();
            assertTrue(nivel >= anterior);
            anterior = nivel;
        }
    }

    @Test
    void removerDoMeioMantemAOrdem() {
        FilaPrioritaria fila = new FilaPrioritaria();
        fila.enfileirar(a(1, "Ana", Prioridade.NORMAL));
        fila.enfileirar(a(2, "Bia", Prioridade.NORMAL));
        fila.enfileirar(a(3, "Caio", Prioridade.PRIORITARIO));
        fila.enfileirar(a(4, "Duda", Prioridade.EMERGENCIA));
        assertTrue(fila.remover(x -> x.id() == 3));
        assertEquals(List.of("Duda", "Ana", "Bia"), nomes(fila));
        assertTrue(fila.remover(x -> x.id() == 4));
        assertEquals(List.of("Ana", "Bia"), nomes(fila));
        assertFalse(fila.remover(x -> x.id() == 99));
    }

    @Test
    void listarNaoMexeNaFilaReal() {
        FilaPrioritaria fila = new FilaPrioritaria();
        fila.enfileirar(a(1, "Ana", Prioridade.NORMAL));
        fila.enfileirar(a(2, "Bia", Prioridade.EMERGENCIA));
        fila.paraLista();
        assertEquals(2, fila.tamanho());
        assertEquals("Bia", fila.primeiro().nome());
    }
}
