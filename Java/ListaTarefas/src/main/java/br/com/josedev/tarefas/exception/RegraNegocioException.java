package br.com.josedev.tarefas.exception;

import org.springframework.http.HttpStatus;

public class RegraNegocioException extends RuntimeException {

    private final HttpStatus status;

    public RegraNegocioException(HttpStatus status, String mensagem) {
        super(mensagem);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
