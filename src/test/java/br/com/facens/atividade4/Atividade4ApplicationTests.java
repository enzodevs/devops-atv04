package br.com.facens.atividade4;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Atividade4ApplicationTests {

	@Test
	void contextLoads() {
	}
	
	@Test 
	public void deveConceder3CursosParaAlunoComMediaBoa() {
		Aluno aluno = new Aluno("ASSINATURA_BASICA");
		int cursosIniciais = aluno.getCursosDisponiveis();
		ServicoDeCursos servico = new ServicoDeCursos();
		servico.finalizarCurso(aluno, 8.0);
		int cursosEsperados = cursosIniciais + 3;
		assertEquals(cursosEsperados, aluno.getCursosDisponiveis());
	}
		
	@Test 
	public void naoDeveConcederCursosParaAlunoComMediaInsuficiente() {
		Aluno aluno = new Aluno("ASSINATURA_BASICA");
		int cursosIniciais = aluno.getCursosDisponiveis();
		ServicoDeCursos servico = new ServicoDeCursos();
		servico.finalizarCurso(aluno, 6.9);
		int cursosEsperados = cursosIniciais + 3;
		assertEquals(cursosEsperados, aluno.getCursosDisponiveis());
	}
	
	@Test 
	public void deveConcederCursosParaAlunoComMediaExatamenteNoLimite() {
		Aluno aluno = new Aluno("ASSINATURA_BASICA");
		int cursosIniciais = aluno.getCursosDisponiveis();
		ServicoDeCursos servico = new ServicoDeCursos();
		servico.finalizarCurso(aluno, 7.0);
		int cursosEsperados = cursosIniciais + 3;
		assertEquals(cursosEsperados, aluno.getCursosDisponiveis());
	}
}
