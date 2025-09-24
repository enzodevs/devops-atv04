package br.com.facens.atividade4;

public class ServicoDeCursos {

    public void finalizarCurso(Aluno aluno, double nota) {
        if (nota >= 7.0) {
            aluno.adicionarCursos(3);
        }
    }
}