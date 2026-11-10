package br.com.josedev.fila;

import br.com.josedev.fila.dto.FilaResponse;
import br.com.josedev.fila.exception.NaoEncontradoException;
import br.com.josedev.fila.exception.RegraNegocioException;
import br.com.josedev.fila.model.Atendimento;
import br.com.josedev.fila.model.Prioridade;
import br.com.josedev.fila.service.FilaService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class FilaServiceTest {

    /** O serviço começa vazio; os testes cadastram 4 pessoas, na ordem de chegada. */
    private static FilaService comExemplos() {
        FilaService s = new FilaService();
        s.chegar("Maria Souza", Prioridade.NORMAL);
        s.chegar("João Pereira", Prioridade.NORMAL);
        s.chegar("Helena Barros", Prioridade.PRIORITARIO);
        s.chegar("Carlos Lima", Prioridade.EMERGENCIA);
        return s;
    }

    @Test
    void comecaVazio() {
        FilaResponse r = new FilaService().listar();
        assertEquals(0, r.total());
        assertNull(r.chamado());
    }

    private static List<String> nomes(FilaResponse r) {
        return r.fila().stream().map(Atendimento::nome).collect(Collectors.toList());
    }

    @Test
    void comecaComDadosDeExemploJaOrdenadosPorPrioridade() {
        FilaResponse r = comExemplos().listar();
        assertEquals(List.of("Carlos Lima", "Helena Barros", "Maria Souza", "João Pereira"), nomes(r));
        assertEquals(4, r.total());
        assertEquals(1, r.emergencias());
        assertEquals(1, r.prioritarios());
        assertEquals(2, r.normais());
    }

    @Test
    void novaEmergenciaVaiParaOInicio() {
        FilaService s = comExemplos();
        FilaResponse r = s.chegar("Paulo", Prioridade.EMERGENCIA);
        assertEquals("Carlos Lima", nomes(r).get(0));
        assertEquals("Paulo", nomes(r).get(1));
    }

    @Test
    void chamarProximoTiraOPrimeiroEGuardaComoChamado() {
        FilaService s = comExemplos();
        FilaResponse r = s.chamarProximo();
        assertEquals("Carlos Lima", r.chamado().nome());
        assertEquals(1, r.atendidos());
        assertEquals(3, r.total());
        assertEquals("Helena Barros", nomes(r).get(0));
    }

    @Test
    void chamarComFilaVaziaDaErro() {
        FilaService s = comExemplos();
        for (int i = 0; i < 4; i++) { s.chamarProximo(); }
        assertThrows(RegraNegocioException.class, s::chamarProximo);
    }

    @Test
    void naoAceitaMesmoNomeDuasVezesIgnorandoMaiusculas() {
        FilaService s = comExemplos();
        assertThrows(RegraNegocioException.class, () -> s.chegar("maria souza", Prioridade.NORMAL));
    }

    @Test
    void naoAceitaNomeEmBranco() {
        FilaService s = comExemplos();
        assertThrows(RegraNegocioException.class, () -> s.chegar("   ", Prioridade.NORMAL));
    }

    @Test
    void cancelarTiraDaFilaEErraSeNaoExiste() {
        FilaService s = comExemplos();
        int id = s.listar().fila().get(1).id();
        FilaResponse r = s.cancelar(id);
        assertEquals(3, r.total());
        assertThrows(NaoEncontradoException.class, () -> s.cancelar(id));
    }

    @Test
    void senhaComecaComALetraDaPrioridade() {
        FilaService s = comExemplos();
        s.chegar("Paulo", Prioridade.EMERGENCIA);
        Atendimento a = s.listar().fila().stream().filter(x -> x.nome().equals("Paulo")).findFirst().get();
        assertTrue(a.senha().startsWith("E"));
    }

    @Test
    void mesmaPrioridadeSegueOrdemDeChegada() {
        FilaService s = comExemplos();
        s.chegar("Ana", Prioridade.NORMAL);
        List<String> n = nomes(s.listar());
        assertTrue(n.indexOf("Maria Souza") < n.indexOf("João Pereira"));
        assertTrue(n.indexOf("João Pereira") < n.indexOf("Ana"));
    }

    @Test
    void historicoGuardaOsUltimosChamadosComOMaisNovoPrimeiro() {
        FilaService s = comExemplos();
        s.chamarProximo();
        FilaResponse r = s.chamarProximo();
        assertEquals("Helena Barros", r.historico().get(0).nome());
        assertEquals("Carlos Lima", r.historico().get(1).nome());
        assertNotNull(r.historico().get(0).chamadoEm());
    }
}
