package br.com.josedev.tarefas.service;

import br.com.josedev.tarefas.dto.TarefaRequest;
import br.com.josedev.tarefas.dto.TarefasResponse;
import br.com.josedev.tarefas.estrutura.ListaEncadeada;
import br.com.josedev.tarefas.exception.NaoEncontradoException;
import br.com.josedev.tarefas.exception.RegraNegocioException;
import br.com.josedev.tarefas.model.Tarefa;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Regras de negócio da lista de tarefas.
 * Os dados ficam guardados NA PRÓPRIA lista encadeada (em memória) — a estrutura de dados é o foco.
 */
@Service
public class TarefaService {

    private final ListaEncadeada<Tarefa> lista = new ListaEncadeada<>();
    private long proximoId = 1;

    public TarefaService() {
        // algumas tarefas de exemplo, pra tela não começar vazia
        lista.adicionarNoFim(new Tarefa(proximoId++, "Estudar Estrutura de Dados"));
        lista.adicionarNoFim(new Tarefa(proximoId++, "Preparar a apresentação"));
        lista.adicionarNoFim(new Tarefa(proximoId++, "Revisar o código"));
    }

    public synchronized TarefasResponse listar() {
        return montarResposta();
    }

    public synchronized TarefasResponse adicionar(TarefaRequest request) {
        String titulo = request.titulo().trim();
        validarTitulo(titulo, null);

        // Regra: sem posição informada, entra no fim da lista
        int posicao = request.posicao() == null ? lista.tamanho() : request.posicao();
        if (posicao < 0 || posicao > lista.tamanho()) {
            throw new RegraNegocioException(HttpStatus.BAD_REQUEST,
                    "Posição inválida. Use um valor de 0 até " + lista.tamanho() + ".");
        }

        lista.inserirNaPosicao(posicao, new Tarefa(proximoId++, titulo));
        return montarResposta();
    }

    public synchronized TarefasResponse editar(Long id, TarefaRequest request) {
        Tarefa tarefa = buscarOuFalhar(id);
        String titulo = request.titulo().trim();
        validarTitulo(titulo, id);

        tarefa.setTitulo(titulo);
        return montarResposta();
    }

    /** Marca como concluída; se já estava concluída, reabre. */
    public synchronized TarefasResponse alternarConcluida(Long id) {
        Tarefa tarefa = buscarOuFalhar(id);
        tarefa.setConcluida(!tarefa.isConcluida());
        return montarResposta();
    }

    public synchronized TarefasResponse remover(Long id) {
        buscarOuFalhar(id);
        lista.remover(t -> t.getId().equals(id));
        return montarResposta();
    }

    /** Remove todas as tarefas concluídas de uma vez. */
    public synchronized TarefasResponse limparConcluidas() {
        int removidas = lista.removerTodos(Tarefa::isConcluida);
        if (removidas == 0) {
            throw new RegraNegocioException(HttpStatus.CONFLICT, "Não há tarefas concluídas para limpar.");
        }
        return montarResposta();
    }

    // ---------- auxiliares ----------

    private Tarefa buscarOuFalhar(Long id) {
        Tarefa tarefa = lista.buscar(t -> t.getId().equals(id));
        if (tarefa == null) {
            throw new NaoEncontradoException("Tarefa " + id + " não encontrada.");
        }
        return tarefa;
    }

    /** Regra: não pode existir duas tarefas com o mesmo título (ignora maiúsculas/minúsculas). */
    private void validarTitulo(String titulo, Long idIgnorado) {
        Tarefa repetida = lista.buscar(t ->
                t.getTitulo().equalsIgnoreCase(titulo) && !t.getId().equals(idIgnorado));
        if (repetida != null) {
            throw new RegraNegocioException(HttpStatus.CONFLICT, "Já existe uma tarefa com esse título.");
        }
    }

    private TarefasResponse montarResposta() {
        List<Tarefa> tarefas = lista.paraLista();
        int concluidas = (int) tarefas.stream().filter(Tarefa::isConcluida).count();
        return new TarefasResponse(tarefas, tarefas.size(), concluidas, tarefas.size() - concluidas);
    }
}
