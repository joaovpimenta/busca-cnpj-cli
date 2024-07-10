package com.joaopimenta.busca_cnpj.clients;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MinhaReceitaBucketClient {
    @GetExchange("/{endpoint}")
    Flux<byte[]> downloadFile(@PathVariable String endpoint);
}
