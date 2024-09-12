package raphaelrocha.desafiotecnico.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raphaelrocha.desafiotecnico.Enums.statusPagamentoEnum;
import raphaelrocha.desafiotecnico.Enums.tipoPagamentoEnums;
import raphaelrocha.desafiotecnico.configs.pagamentos;
import raphaelrocha.desafiotecnico.exceptions.*;
import raphaelrocha.desafiotecnico.repository.pagamentosRepositorio;
import raphaelrocha.desafiotecnico.request.pagamentosRequest;
import raphaelrocha.desafiotecnico.validadores.validadorCpfCnpj;
import raphaelrocha.desafiotecnico.validadores.validadorCartao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class pagamentosService {

    @Autowired
    private pagamentosRepositorio pagamentos_repositorio;

    public long gerarNovoPagamento(pagamentosRequest pagamentos_request){

        pagamentos pagamento = new pagamentos();
        if(!validadorCpfCnpj.isValid(pagamentos_request.getCpfcnpj())){
            throw new CpfCnpjNaoValido(String.format("cpf ou cnpj: '%s' não é valido", pagamentos_request.getCpfcnpj()));
        }

        pagamento.setCpfcnpj(pagamentos_request.getCpfcnpj().trim().replace(".", "").replace("-", "").replace("/", "").replace("–", ""));
        pagamento.setMet_pag(pagamentos_request.getMet_pag());
        pagamento.setVal_pag(pagamentos_request.getVal_pag());
        pagamento.setStatusPag(statusPagamentoEnum.PENDENTE_DE_PROCESSAMENTO);
        pagamento.setNum_cartao("");

        if((tipoPagamentoEnums.CARTAO_CREDITO.equals(pagamentos_request.getMet_pag()) || (tipoPagamentoEnums.CARTAO_DEBITO.equals(pagamentos_request.getMet_pag())))){
            pagamentos_request.setNum_cartao(pagamentos_request.getNum_cartao().trim().replace(".", "").replace("-", "").replace("/", "").replace("–", ""));
            if(!validadorCartao.isValid(pagamentos_request.getNum_cartao())){
                throw new CartaoNaoValido(String.format("Cartao: '%s' não é valido", pagamentos_request.getNum_cartao()));
            }
            pagamento.setNum_cartao(pagamentos_request.getNum_cartao());
        }

        pagamento = pagamentos_repositorio.save(pagamento);
        return pagamento.getCod_deb();
    }

    public List<pagamentos> buscaTodosPagamentos(){
        return pagamentos_repositorio.findAll();
    }

    public List<pagamentos> buscaTodosPagamentosFiltro(Long cod_deb, String cpf_cnpj, statusPagamentoEnum statusPag){
        List<pagamentos> pagamentosFiltrados = new ArrayList<>();

        if (cod_deb != null) {
            Optional<pagamentos> pagamentoRequisitadoOpt = pagamentos_repositorio.findById(cod_deb);
            pagamentoRequisitadoOpt.ifPresent(pagamentosFiltrados::add);
        }

        if (statusPag != null) {
            List<pagamentos> pagamentosPorStatus = pagamentos_repositorio.findByStatusPag(statusPag);
            if (pagamentosFiltrados.isEmpty()) {
                pagamentosFiltrados.addAll(pagamentosPorStatus);
            } else {
                pagamentosFiltrados = pagamentosFiltrados.stream()
                        .filter(pagamentosPorStatus::contains)
                        .collect(Collectors.toList());
            }
        }

        if (cpf_cnpj != null && !cpf_cnpj.isEmpty()) {
            List<pagamentos> pagamentosPorCPF = pagamentos_repositorio.findBycpfcnpj(cpf_cnpj);
            if (pagamentosFiltrados.isEmpty()) {
                pagamentosFiltrados.addAll(pagamentosPorCPF);
            } else {
                pagamentosFiltrados = pagamentosFiltrados.stream()
                        .filter(pagamentosPorCPF::contains)
                        .collect(Collectors.toList());
            }
        }
        if(cod_deb == null && statusPag == null && cpf_cnpj == null){
            pagamentosFiltrados = buscaTodosPagamentos();
        }
        return pagamentosFiltrados;
    }

    @Transactional
    public pagamentos atualizaPagamento (Long cod_deb, statusPagamentoEnum statusPag){
        Optional<pagamentos> pagamentoRequisitado = pagamentos_repositorio.findById(cod_deb);

        if(pagamentoRequisitado.isEmpty()){
            throw new  PagamentoNaoEncontradoException(String.format("Pagamento com o código de pagamento: '%s' não encontrado", cod_deb));
        }

        pagamentos pagamento = pagamentoRequisitado.get();

        if(pagamento.isStatusDebitoDesativado()) {
            throw new PagamentoExcluidoLogicamente(String.format("Pagamento com o código de pagamento: '%s' se encontra desativado", cod_deb));
        }

        if(statusPagamentoEnum.PROCESSADO_COM_SUCESSO.equals(pagamento.getStatusPag())){
            throw new PagamentoJaEstaProcessadoComSucessoException(String.format("Pagamento de código de debito: '%s' já se encontra Processado com sucesso", cod_deb));
        }
        else if(statusPagamentoEnum.PROCESSADO_COM_FALHA.equals(pagamento.getStatusPag())){
            if((statusPagamentoEnum.PENDENTE_DE_PROCESSAMENTO.equals(statusPag))){
                pagamento.setStatusPag(statusPag);
                return pagamento;
            }else{
                throw new PagamentoJaEstaProcessadoComSucessoException(String.format("Pagamento de código de debito: '%s' está processado com falha" +
                        " é necessário atualizar primeiramente para pendente de processamento'", cod_deb));
            }
        }

        pagamento.setStatusPag(statusPag);
        return pagamento;
    }

    @Transactional
    public pagamentos desativaDebitoPorCod_pag(Long cod_deb){
        Optional<pagamentos> pagamentoRequisitado = pagamentos_repositorio.findById(cod_deb);
        if(pagamentoRequisitado.isEmpty()){
            throw new  PagamentoNaoEncontradoException(String.format("Pagamento com o código de pagamento: '%s' não encontrado", cod_deb));
        }
        pagamentos pagamento = pagamentoRequisitado.get();
        if(statusPagamentoEnum.PENDENTE_DE_PROCESSAMENTO.equals(pagamento.getStatusPag())){
            if(pagamento.isStatusDebitoDesativado()) {
                throw new PagamentoExcluidoLogicamente(String.format("Pagamento com o código de pagamento: '%s' se encontra desativado", cod_deb));
            }
            pagamento.setStatusDebitoDesativado(true);
            return pagamento;
        }else{
            throw new PagamentoJaEstaProcessadoComSucessoException(String.format("Pagamento de código de debito: '%s' já se encontra Processado com sucesso ou com falha", cod_deb));
        }
    }
}
