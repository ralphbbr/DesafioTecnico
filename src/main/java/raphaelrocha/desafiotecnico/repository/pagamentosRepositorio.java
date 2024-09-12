package raphaelrocha.desafiotecnico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import raphaelrocha.desafiotecnico.Enums.statusPagamentoEnum;
import raphaelrocha.desafiotecnico.configs.pagamentos;

import java.util.List;

public interface pagamentosRepositorio extends JpaRepository<pagamentos, Long> {
    List<pagamentos> findBycpfcnpj(String cpfcnpj);
    List<pagamentos> findByStatusPag(statusPagamentoEnum statusPag);
}
