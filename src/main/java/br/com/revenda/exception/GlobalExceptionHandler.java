package br.com.revenda.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> tratarValidacao(MethodArgumentNotValidException exception) {

        String mensagem = exception
                .getBindingResult()
                .getFieldErrors()
                .get(0)
                .getField() + ": " +
                exception
                        .getBindingResult()
                        .getFieldErrors()
                        .get(0)
                        .getDefaultMessage();

        List<String> erros = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .toList();

        return ResponseEntity.badRequest().body(erros);
    }

}
