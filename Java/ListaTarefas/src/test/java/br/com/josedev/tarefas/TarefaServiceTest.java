package br.com.josedev.tarefas;

import br.com.josedev.tarefas.dto.TarefaRequest;
import br.com.josedev.tarefas.dto.TarefasResponse;
import br.com.josedev.tarefas.exception.NaoEncontradoException;
import br.com.josedev.tarefas.exception.RegraNegocioException;
import br.com.josedev.tarefas.model.Tarefa;
import br.com.josedev.tarefas.service.TarefaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TarefaServiceTest {

    private TarefaService service;

    @BeforeEach
    void preparar() {
        service = new TarefaService();   // começa com 3 tarefas de exemplo
    }

    private List<String> titulos(TarefasResponse r) {
        return r.tarefas().stream().map(Tarefa::getTitulo).toList();
    }

    @Test
    void comecaComTresTarefasDeExemplo() {
        TarefasResponse r = service.listar();
        assertEquals(3, r.total());
        assertEquals(3, r.pendentes());
        assertEquals(0, r.concluidas());
    }

    @Test
    void semPosicaoEntraNoFim() {
        TarefasResponse r = service.adicionar(new TarefaRequest("Nova", null));
        assertEquals("Nova", titulos(r).get(3));
    }

    @Test
    void posicaoZeroEntraNoInicio() {
        TarefasResponse r = service.adicionar(new TarefaRequest("Urgente", 0));
        assertEquals("Urgente", titulos(r).get(0));
    }

    @Test
    void posicaoNoMeio() {
        TarefasResponse r = service.adicionar(new TarefaRequest("Meio", 1));
        assertEquals("Meio", titulos(r).get(1));
        assertEquals(4, r.total());
    }

    @Test
    void posicaoInvalidaDaErro() {
        assertThrows(RegraNegocioException.class, () -> service.adicionar(new TarefaRequest("X", 99)));
        assertThrows(RegraNegocioException.class, () -> service.adicionar(new TarefaRequest("X", -1)));
    }

    @Test
    void naoPermiteTituloRepetidoIgnorandoMaiusculas() {
        assertThrows(RegraNegocioException.class,
                () -> service.adicionar(new TarefaRequest("revisar o código", null)));
    }

    @Test
    void tituloDeveSerTrimado() {
        TarefasResponse r = service.adicionar(new TarefaRequest("   Com espaços   ", null));
        assertEquals("Com espaços", titulos(r).get(3));
    }

    @Test
    void alternaConcluida() {
        Long id = service.listar().tarefas().get(0).getId();

        TarefasResponse r = service.alternarConcluida(id);
        assertEquals(1, r.concluidas());

        r = service.alternarConcluida(id);   // reabre
        assertEquals(0, r.concluidas());
    }

    @Test
    void editaTituloMasNaoParaUmRepetido() {
        List<Tarefa> tarefas = service.listar().tarefas();
        Long id = tarefas.get(0).getId();

        TarefasResponse r = service.editar(id, new TarefaRequest("Título novo", null));
        assertEquals("Título novo", titulos(r).get(0));

        // pode "editar" mantendo o mesmo título dela mesma
        assertDoesNotThrow(() -> service.editar(id, new TarefaRequest("Título novo", null)));

        // mas não pode virar o título de outra
        assertThrows(RegraNegocioException.class,
                () -> service.editar(id, new TarefaRequest("Revisar o código", null)));
    }

    @Test
    void removeTarefa() {
        Long id = service.listar().tarefas().get(1).getId();

        TarefasResponse r = service.remover(id);

        assertEquals(2, r.total());
        assertThrows(NaoEncontradoException.class, () -> service.remover(id));
    }

    @Test
    void limpaConcluidas() {
        List<Tarefa> tarefas = service.listar().tarefas();
        service.alternarConcluida(tarefas.get(0).getId());
        service.alternarConcluida(tarefas.get(2).getId());

        TarefasResponse r = service.limparConcluidas();

        assertEquals(1, r.total());
        assertEquals("Preparar a apresentação", titulos(r).get(0));
    }

    @Test
    void limparSemConcluidasDaErro() {
        assertThrows(RegraNegocioException.class, () -> service.limparConcluidas());
    }
}
