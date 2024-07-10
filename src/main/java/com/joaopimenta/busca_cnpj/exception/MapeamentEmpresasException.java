package com.joaopimenta.busca_cnpj.exception;

public class MapeamentEmpresasException extends RuntimeException {
    public MapeamentEmpresasException(String mensagem, Exception e) {
        super(mensagem, e);
    }
}
