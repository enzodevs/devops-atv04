package br.com.facens.atividade4.application.exception;

public class EmailAlunoJaCadastradoException extends RuntimeException {
    public EmailAlunoJaCadastradoException(String email) {
        super("E-mail já cadastrado para outro aluno: " + email);
    }
}
