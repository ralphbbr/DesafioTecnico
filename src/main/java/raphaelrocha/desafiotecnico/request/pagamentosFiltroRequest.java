package raphaelrocha.desafiotecnico.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;
import lombok.Data;
import raphaelrocha.desafiotecnico.Enums.statusPagamentoEnum;

@Data
public class pagamentosFiltroRequest {

    @Size(max = 20)
    @Nullable
    private String cpfcnpj;

    @Nullable
    private Long codDeb;

    @Nullable
    private statusPagamentoEnum statusPag;



}
