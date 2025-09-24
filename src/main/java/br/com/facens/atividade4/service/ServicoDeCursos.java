package br.com.facens.atividade4.service;

import br.com.facens.atividade4.domain.Aluno;
import br.com.facens.atividade4.domain.RegraGamificacao;

public class ServicoDeCursos {

    public void finalizarCurso(Aluno aluno, double nota) {
        validarParametros(aluno, nota);

        if (aluno.podeReceberBonus() && RegraGamificacao.aprovado(nota)) {
            int cursosBonus = RegraGamificacao.calcularCursosBonus(nota);
            aluno.adicionarCursos(cursosBonus);
        }
    }

    private void validarParametros(Aluno aluno, double nota) {
        if (aluno == null) {
            throw new IllegalArgumentException("Aluno não pode ser nulo");
        }
        if (nota < 0 || nota > 10) {
            throw new IllegalArgumentException("Nota deve estar entre 0 e 10");
        }
    }
}