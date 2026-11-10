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
    void removerDoMeioDoInicioEDoFim() {
        FilaPrioritaria fila = new FilaPrioritaria();
        fila.enfileirar(a(1, "Ana", Prioridade.NORMAL));
        fila.enfileirar(a(2, "Bia", Prioridade.NORMAL));
        fila.enfileirar(a(3, "Caio", Prioridade.NORMAL));
        assertTrue(fila.remover(x -> x.id() == 2));
        assertEquals(List.of("Ana", "Caio"), nomes(fila));
        assertTrue(fila.remover(x -> x.id() == 3));
        fila.enfileirar(a(4, "Duda", Prioridade.NORMAL));
        assertEquals(List.of("Ana", "Duda"), nomes(fila));
        assertTrue(fila.remover(x -> x.id() == 1));
        assertEquals(List.of("Duda"), nomes(fila));
        assertFalse(fila.remover(x -> x.id() == 99));
    }
}
