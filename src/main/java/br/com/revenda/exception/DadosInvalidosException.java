package br.com.revenda.exception;

public class DadosInvalidosException extends RuntimeException{

    public DadosInvalidosException(String mensagem){
        super(mensagem);
    }
}
