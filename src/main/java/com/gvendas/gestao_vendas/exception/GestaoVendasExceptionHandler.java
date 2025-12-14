package com.gvendas.gestao_vendas.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GestaoVendasExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String CONSTANT_VALIDATION_NOT_BLANK = "NotBlank";
    private static final String CONSTANT_VALIDATION_NOT_NULL = "NotNull";
    private static final String CONSTANT_VALIDATION_LENGTH = "Length";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<Erro> erros = gerarListaDeErros(ex.getBindingResult());

        return handleExceptionInternal(ex, erros, headers, status, request);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<Object> handleEmptyResultDataAcessException(EmptyResultDataAccessException ex,
            WebRequest request) {
        String msgUsuario = "Recurso não encontrado";
        String msgDesenvolvedor = ex.toString();
        List<Erro> erros = Arrays.asList(new Erro(msgUsuario, msgDesenvolvedor));
        return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex,
            WebRequest request) {
        String msgUsuario = "Recurso não encontrado";
        String msgDesenvolvedor = ex.toString();
        List<Erro> erros = Arrays.asList(new Erro(msgUsuario, msgDesenvolvedor));
        return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<Object> handleRegraNegocioException(RegraNegocioException ex, WebRequest request) {
        String msgUsuario = ex.getMessage();
        String msgDesenvolvedor = ex.getMessage();
        List<Erro> erros = Arrays.asList(new Erro(msgUsuario, msgDesenvolvedor));
        return handleExceptionInternal(ex, erros, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    private List<Erro> gerarListaDeErros(BindingResult bindingResult) {
        List<Erro> erros = new ArrayList<Erro>();
        bindingResult.getFieldErrors().forEach(fielError -> {

            String msgUsario = tratarMensagemDeErroParaUsuario(fielError);
            String msgDesenvolvedor = fielError.toString();
            erros.add(new Erro(msgUsario, msgDesenvolvedor));

        });
        return erros;
    }

    private String tratarMensagemDeErroParaUsuario(FieldError fielError) {
        if (fielError.getCode().equals(CONSTANT_VALIDATION_NOT_BLANK)) {
            return fielError.getDefaultMessage().concat(" É obrigatório.");
        }

        if (fielError.getCode().equals(CONSTANT_VALIDATION_NOT_NULL)) {
            return fielError.getDefaultMessage().concat(" É obrigatório.");
        }

        if (fielError.getCode().equals(CONSTANT_VALIDATION_LENGTH))
            return fielError.getDefaultMessage().concat(String.format(" Deve ter entre %s e %s caracteres. ",
                    fielError.getArguments()[2], fielError.getArguments()[1]));
        return fielError.toString();
    }
}
