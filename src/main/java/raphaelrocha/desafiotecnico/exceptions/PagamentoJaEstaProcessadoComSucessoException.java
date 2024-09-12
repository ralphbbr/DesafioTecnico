package raphaelrocha.desafiotecnico.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.ALREADY_REPORTED)
public class PagamentoJaEstaProcessadoComSucessoException extends RuntimeException {
    public PagamentoJaEstaProcessadoComSucessoException(String message) {
        super(message);
    }
}
