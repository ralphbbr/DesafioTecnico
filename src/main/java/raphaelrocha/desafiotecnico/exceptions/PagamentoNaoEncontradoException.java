package raphaelrocha.desafiotecnico.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PagamentoNaoEncontradoException extends RuntimeException {
    public PagamentoNaoEncontradoException(String message) {
        super(message);
    }
}
