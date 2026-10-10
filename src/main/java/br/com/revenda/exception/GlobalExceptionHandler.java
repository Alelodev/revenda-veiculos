package br.com.revenda.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<String> tratarRecursoNaoEncontrado(RecursoNaoEncontradoException exception){
        return ResponseEntity.status(404).body(exception.getMessage());
    }

    @ExceptionHandler(DadosInvalidosException.class)
    public ResponseEntity<String> tratarDadosInvalido(DadosInvalidosException excpetion){
        return ResponseEntity.status(400).body(excpetion.getMessage());
    }


}
