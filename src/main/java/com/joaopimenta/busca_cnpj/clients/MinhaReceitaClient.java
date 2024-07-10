package com.joaopimenta.busca_cnpj.clients;


import com.joaopimenta.busca_cnpj.records.DataAtualizacao;
import org.springframework.http.MediaType;
import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Mono;

import java.util.List;

public interface MinhaReceitaClient {

    @GetExchange(value = "/dados/INTERMED/CAD/DADOS/cad_intermed.zip", accept = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    Mono<byte[]> getDadosCNPJ();

    @GetExchange("/updated")
    Mono<String> getDataDeAtualizacao();
}
