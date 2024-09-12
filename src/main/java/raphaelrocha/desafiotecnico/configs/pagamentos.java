package raphaelrocha.desafiotecnico.configs;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;
import raphaelrocha.desafiotecnico.Enums.statusPagamentoEnum;
import raphaelrocha.desafiotecnico.Enums.tipoPagamentoEnums;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "pagamentos", uniqueConstraints = {@UniqueConstraint(columnNames = {"cod_deb"})})
public class pagamentos {

    @Id
    @GeneratedValue
    private Long cod_deb;

    @Column(nullable = false)
    @Size(max = 20)
    private String cpfcnpj;

    @Column(nullable = false)
    private tipoPagamentoEnums met_pag;

    @Column(nullable = false)
    private statusPagamentoEnum statusPag;

    @Column(nullable = false)
    private String num_cartao;

    @Column(nullable = false)
    private double val_pag;

    @Column(nullable = false)
    private boolean statusDebitoDesativado;

}
