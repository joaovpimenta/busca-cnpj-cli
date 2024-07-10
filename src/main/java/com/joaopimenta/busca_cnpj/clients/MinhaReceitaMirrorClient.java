package com.joaopimenta.busca_cnpj.clients;

import org.springframework.http.MediaType;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

public interface MinhaReceitaMirrorClient {

    @GetExchange(accept = MediaType.APPLICATION_JSON_VALUE)
    String getUrlsDownload();

}
