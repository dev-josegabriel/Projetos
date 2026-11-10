package br.com.josedev.tarefas.exception;

public class NaoEncontradoException extends RuntimeException {

    public NaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
