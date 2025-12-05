package br.com.facens.atividade4.presentation.handler;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.facens.atividade4.application.exception.AlunoNaoEncontradoException;
import br.com.facens.atividade4.application.exception.EmailAlunoJaCadastradoException;

@RestControllerAdvice
public class ApiExceptionHandler {

    // Traduz exceções de domínio/validação em respostas HTTP padronizadas (404, 409, 400).
    @ExceptionHandler(AlunoNaoEncontradoException.class)
    public ResponseEntity<ApiErrorResponse> tratarAlunoNaoEncontrado(AlunoNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiErrorResponse.of(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(EmailAlunoJaCadastradoException.class)
    public ResponseEntity<ApiErrorResponse> tratarEmailDuplicado(EmailAlunoJaCadastradoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiErrorResponse.of(HttpStatus.CONFLICT, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> tratarValidacao(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        String mensagem = bindingResult.getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Erro de validação");

        return ResponseEntity.badRequest()
                .body(ApiErrorResponse.of(HttpStatus.BAD_REQUEST, mensagem));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> tratarRegraNegocio(IllegalArgumentException ex) {
        return ResponseEntity.badRequest()
                .body(ApiErrorResponse.of(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    public record ApiErrorResponse(int status, String error, String message, Instant timestamp) {
        private static ApiErrorResponse of(HttpStatus status, String message) {
            return new ApiErrorResponse(status.value(), status.getReasonPhrase(), message, Instant.now());
        }
    }
}
