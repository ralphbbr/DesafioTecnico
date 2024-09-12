package raphaelrocha.desafiotecnico.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class PagamentoProcessadoComFalhaNaoPodeProcessarComSucesso extends RuntimeException {
    public PagamentoProcessadoComFalhaNaoPodeProcessarComSucesso(String message) {
        super(message);
    }
}
