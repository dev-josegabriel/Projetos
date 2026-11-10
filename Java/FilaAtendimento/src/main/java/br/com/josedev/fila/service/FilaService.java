package br.com.josedev.fila.service;

import br.com.josedev.fila.dto.FilaResponse;
import br.com.josedev.fila.exception.NaoEncontradoException;
import br.com.josedev.fila.exception.RegraNegocioException;
import br.com.josedev.fila.model.Atendimento;
import br.com.josedev.fila.model.Prioridade;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Regras de negócio da fila. Os dados ficam só na memória, numa lista simples. */
@Service
public class FilaService {

    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");

    /** Emergência primeiro, depois prioritário, depois normal. No mesmo nível, quem chegou antes (menor id). */
    private static final Comparator<Atendimento> ORDEM_DE_CHAMADA =
            Comparator.comparingInt((Atendimento a) -> a.prioridade().getNivel()).thenComparingInt(Atendimento::id);

    private final List<Atendimento> fila = new ArrayList<>();
    private final List<Atendimento> historico = new ArrayList<>();
    private int proximoId = 1;
    private int atendidos = 0;
    private Atendimento chamado = null;

    public FilaService() {
        // Dados de exemplo, na ordem de chegada. Repare que a fila mostra por prioridade.
        chegar("Maria Souza", Prioridade.NORMAL);
        chegar("João Pereira", Prioridade.NORMAL);
        chegar("Helena Barros", Prioridade.PRIORITARIO);
        chegar("Carlos Lima", Prioridade.EMERGENCIA);
    }

    public synchronized FilaResponse listar() {
        return resposta();
    }

    public synchronized FilaResponse chegar(String nome, Prioridade prioridade) {
        String limpo = nome == null ? "" : nome.trim();
        if (limpo.isEmpty()) {
            throw new RegraNegocioException(HttpStatus.BAD_REQUEST, "O nome é obrigatório.");
        }
        if (prioridade == null) {
            throw new RegraNegocioException(HttpStatus.BAD_REQUEST, "Escolha a prioridade.");
        }
        if (fila.stream().anyMatch(a -> a.nome().equalsIgnoreCase(limpo))) {
            throw new RegraNegocioException(HttpStatus.CONFLICT, "Essa pessoa já está na fila.");
        }
        int id = proximoId++;
        String senha = prioridade.name().charAt(0) + String.format("%03d", id);
        fila.add(new Atendimento(id, senha, limpo, prioridade, LocalTime.now().format(HORA)));
        return resposta();
    }

    /** Chama quem está no início da fila. */
    public synchronized FilaResponse chamarProximo() {
        if (fila.isEmpty()) {
            throw new RegraNegocioException(HttpStatus.CONFLICT, "Não há ninguém na fila.");
        }
        Atendimento proximo = ordenada().get(0);
        fila.remove(proximo);
        chamado = proximo;
        historico.add(0, proximo);
        atendidos++;
        return resposta();
    }

    /** Pessoa que desistiu e saiu da fila. */
    public synchronized FilaResponse cancelar(int id) {
        if (!fila.removeIf(a -> a.id() == id)) {
            throw new NaoEncontradoException("Essa pessoa não está mais na fila.");
        }
        return resposta();
    }

    private List<Atendimento> ordenada() {
        List<Atendimento> copia = new ArrayList<>(fila);
        copia.sort(ORDEM_DE_CHAMADA);
        return copia;
    }

    private FilaResponse resposta() {
        List<Atendimento> lista = ordenada();
        long emergencias = lista.stream().filter(a -> a.prioridade() == Prioridade.EMERGENCIA).count();
        long prioritarios = lista.stream().filter(a -> a.prioridade() == Prioridade.PRIORITARIO).count();
        long normais = lista.stream().filter(a -> a.prioridade() == Prioridade.NORMAL).count();
        return new FilaResponse(lista, historico.stream().limit(5).toList(), lista.size(), (int) emergencias, (int) prioritarios, (int) normais, atendidos, chamado);
    }
}
