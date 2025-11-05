package br.com.facens.atividade4.application.exception;

public class AlunoNaoEncontradoException extends RuntimeException {
    public AlunoNaoEncontradoException(Long id) {
        super("Aluno não encontrado para o identificador " + id);
    }
}
