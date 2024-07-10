package com.joaopimenta.busca_cnpj.records;

public record DataAtualizacao(String message) {
    public String getData() {
        return message.substring(0,10);
    }
}
