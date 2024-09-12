package raphaelrocha.desafiotecnico.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import raphaelrocha.desafiotecnico.Enums.tipoPagamentoEnums;
import raphaelrocha.desafiotecnico.Enums.statusPagamentoEnum;

@Data
public class pagamentosRequest {

    @NotEmpty
    @Size(max = 20)
    private String cpfcnpj;

    private tipoPagamentoEnums met_pag;

    @Nullable
    private String num_cartao;

    @NotNull
    private double val_pag;

}
