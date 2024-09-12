package raphaelrocha.desafiotecnico.controller;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import raphaelrocha.desafiotecnico.Enums.statusPagamentoEnum;
import raphaelrocha.desafiotecnico.configs.pagamentos;
import raphaelrocha.desafiotecnico.request.codDebitoRequest;
import raphaelrocha.desafiotecnico.request.pagamentosRequest;
import raphaelrocha.desafiotecnico.request.statusPagamentoRequest;
import raphaelrocha.desafiotecnico.services.pagamentosService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/pagamentos")
public class pagamentosController {

    @Autowired
    private pagamentosService pagamentos_service;

    @PostMapping
    public ResponseEntity<Void> geraNovoPagamento(@Valid @RequestBody pagamentosRequest pagamentos_request, UriComponentsBuilder uriComponentsBuilder){
        Long cod_deb = pagamentos_service.gerarNovoPagamento(pagamentos_request);
        UriComponents uriComponents = uriComponentsBuilder.path("/api/pagamentos/{cod_deb}").buildAndExpand(cod_deb);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponents.toUri());

        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @GetMapping("/buscaPagamentos")
    public ResponseEntity<List<pagamentos>> buscaPagamentoFiltro(@Nullable Long codDeb,@Nullable String cpfCnpj,@Nullable statusPagamentoEnum statusPag){
        return ResponseEntity.ok(pagamentos_service.buscaTodosPagamentosFiltro(codDeb, cpfCnpj, statusPag));
    }

    @PutMapping("/atualizaStatusPagamento")
    public ResponseEntity<pagamentos> atualizaPagamento(@Valid @RequestBody statusPagamentoRequest statusPagamento_request, UriComponentsBuilder uriComponentsBuilder){
            return ResponseEntity.ok(pagamentos_service.atualizaPagamento(statusPagamento_request.getCodDeb(),statusPagamento_request.getStatusPag()));
    }

    @PutMapping("/desativarDebito")
    public ResponseEntity<pagamentos> desativaPagamento(@Valid @RequestBody codDebitoRequest codDebRequest, UriComponentsBuilder uriComponentsBuilder){
        return ResponseEntity.ok(pagamentos_service.desativaDebitoPorCod_pag(codDebRequest.getCodDeb()));
    }
}
