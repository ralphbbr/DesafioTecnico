package raphaelrocha.desafiotecnico.initializer;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import raphaelrocha.desafiotecnico.Enums.statusPagamentoEnum;
import raphaelrocha.desafiotecnico.Enums.tipoPagamentoEnums;
import raphaelrocha.desafiotecnico.repository.pagamentosRepositorio;
import raphaelrocha.desafiotecnico.configs.pagamentos;

@Slf4j
@Component
public class pagamentos_inicializador implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(pagamentos_inicializador.class);

    @Autowired
    private pagamentosRepositorio pagamentos_repositorio;


    @Override
    public void run(String... args) throws Exception {

        log.info("Adicionando Base de Testes...");

        for(int i=0; i < 10; i++ ){

            pagamentos pagamento = new pagamentos();

            pagamento.setCpfcnpj("12345678909");
            pagamento.setNum_cartao("");
            pagamento.setMet_pag(tipoPagamentoEnums.PIX);
            pagamento.setStatusPag(statusPagamentoEnum.PENDENTE_DE_PROCESSAMENTO);
            pagamento.setVal_pag(12.32);
            pagamentos_repositorio.save(pagamento);
        }

        log.info("...Base de Testes Adicionada");

    }
}
