package raphaelrocha.desafiotecnico.request;

import lombok.Data;
import raphaelrocha.desafiotecnico.Enums.statusPagamentoEnum;

@Data
public class statusPagamentoRequest {

    private Long codDeb;
    private statusPagamentoEnum statusPag;

}
