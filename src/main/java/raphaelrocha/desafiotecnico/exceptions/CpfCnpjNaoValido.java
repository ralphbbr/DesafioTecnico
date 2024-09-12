package raphaelrocha.desafiotecnico.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CpfCnpjNaoValido extends RuntimeException {
    public CpfCnpjNaoValido(String message) {
        super(message);
    }
}
