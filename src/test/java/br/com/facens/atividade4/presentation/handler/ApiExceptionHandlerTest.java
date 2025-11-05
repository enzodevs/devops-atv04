package br.com.facens.atividade4.presentation.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import br.com.facens.atividade4.application.exception.AlunoNaoEncontradoException;
import br.com.facens.atividade4.application.exception.EmailAlunoJaCadastradoException;

class ApiExceptionHandlerTest {

    private final ApiExceptionHandler handler = new ApiExceptionHandler();

    @Test
    void deveTratarAlunoNaoEncontrado() {
        var response = handler.tratarAlunoNaoEncontrado(new AlunoNaoEncontradoException(1L));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertThat(response.getBody().message()).contains("1");
    }

    @Test
    void deveTratarEmailDuplicado() {
        var response = handler.tratarEmailDuplicado(new EmailAlunoJaCadastradoException("duplicado@example.com"));

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertThat(response.getBody().message()).contains("duplicado@example.com");
    }

    @Test
    void deveTratarValidacao() throws NoSuchMethodException {
        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(new Object(), "aluno");
        bindingResult.addError(new FieldError("aluno", "email", "Email é obrigatório"));

        Method method = Dummy.class.getDeclaredMethod("metodo", String.class);
        MethodParameter parameter = new MethodParameter(method, 0);

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(parameter, bindingResult);

        var response = handler.tratarValidacao(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertThat(response.getBody().message()).contains("email");
    }

    @Test
    void deveTratarRegraDeNegocio() {
        var response = handler.tratarRegraNegocio(new IllegalArgumentException("Regra violada"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertThat(response.getBody().message()).isEqualTo("Regra violada");
    }

    private static class Dummy {
        @SuppressWarnings("unused")
        void metodo(String valor) {
            // usado apenas para obter MethodParameter
        }
    }
}
