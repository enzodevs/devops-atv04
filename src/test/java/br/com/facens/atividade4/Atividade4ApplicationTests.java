package br.com.facens.atividade4;

import br.com.facens.atividade4.domain.Aluno;
import br.com.facens.atividade4.service.ServicoDeCursos;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
		assertEquals(cursosIniciais, aluno.getCursosDisponiveis());
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

	@Test
	public void deveValidarTipoAssinaturaPremium() {
		Aluno aluno = new Aluno("ASSINATURA_PREMIUM");
		assertEquals(10, aluno.getCursosDisponiveis());
	}

	@Test
	public void deveLancarExcecaoParaTipoAssinaturaInvalido() {
		try {
			new Aluno("ASSINATURA_INVALIDA");
			assert false : "Deveria lançar exceção";
		} catch (IllegalArgumentException e) {
			assertEquals("Tipo de assinatura inválido: ASSINATURA_INVALIDA", e.getMessage());
		}
	}

	@Test
	public void deveLancarExcecaoParaAlunoNulo() {
		ServicoDeCursos servico = new ServicoDeCursos();
		try {
			servico.finalizarCurso(null, 8.0);
			assert false : "Deveria lançar exceção";
		} catch (IllegalArgumentException e) {
			assertEquals("Aluno não pode ser nulo", e.getMessage());
		}
	}

	@Test
	public void deveLancarExcecaoParaNotaInvalida() {
		Aluno aluno = new Aluno("ASSINATURA_BASICA");
		ServicoDeCursos servico = new ServicoDeCursos();
		try {
			servico.finalizarCurso(aluno, 11.0);
			assert false : "Deveria lançar exceção";
		} catch (IllegalArgumentException e) {
			assertEquals("Nota deve estar entre 0 e 10", e.getMessage());
		}
	}
}
