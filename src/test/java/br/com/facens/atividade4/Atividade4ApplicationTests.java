package br.com.facens.atividade4;

import br.com.facens.atividade4.domain.Aluno;
import br.com.facens.atividade4.domain.ContatoEmail;
import br.com.facens.atividade4.domain.TipoAssinatura;
import br.com.facens.atividade4.service.ServicoDeCursos;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
class Atividade4ApplicationTests {

	@Test
	void contextLoads() {
	}
	
	@Test 
	public void deveConceder3CursosParaAlunoComMediaBoa() {
		Aluno aluno = novoAlunoBasico("media.boa@example.com");
		int cursosIniciais = aluno.getCursosDisponiveis();
		ServicoDeCursos servico = new ServicoDeCursos();
		servico.finalizarCurso(aluno, 8.0);
		int cursosEsperados = cursosIniciais + 3;
		assertEquals(cursosEsperados, aluno.getCursosDisponiveis());
	}
		
	@Test
	public void naoDeveConcederCursosParaAlunoComMediaInsuficiente() {
		Aluno aluno = novoAlunoBasico("media.insuficiente@example.com");
		int cursosIniciais = aluno.getCursosDisponiveis();
		ServicoDeCursos servico = new ServicoDeCursos();
		servico.finalizarCurso(aluno, 6.9);
		assertEquals(cursosIniciais, aluno.getCursosDisponiveis());
	}
	
	@Test
	public void deveConcederCursosParaAlunoComMediaExatamenteNoLimite() {
		Aluno aluno = novoAlunoBasico("media.limite@example.com");
		int cursosIniciais = aluno.getCursosDisponiveis();
		ServicoDeCursos servico = new ServicoDeCursos();
		servico.finalizarCurso(aluno, 7.0);
		int cursosEsperados = cursosIniciais + 3;
		assertEquals(cursosEsperados, aluno.getCursosDisponiveis());
	}

	@Test
	public void deveValidarTipoAssinaturaPremium() {
		Aluno aluno = Aluno.novo("Aluno Premium", ContatoEmail.of("premium@example.com"),
				TipoAssinatura.ASSINATURA_PREMIUM);
		assertEquals(10, aluno.getCursosDisponiveis());
	}

	@Test
	public void deveLancarExcecaoParaTipoAssinaturaInvalido() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> TipoAssinatura.fromString("ASSINATURA_INVALIDA"));

		assertEquals("Tipo de assinatura inválido: ASSINATURA_INVALIDA", exception.getMessage());
	}

	@Test
	public void deveLancarExcecaoParaAlunoNulo() {
		ServicoDeCursos servico = new ServicoDeCursos();
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> servico.finalizarCurso(null, 8.0));

		assertEquals("Aluno não pode ser nulo", exception.getMessage());
	}

	@Test
	public void deveLancarExcecaoParaNotaInvalida() {
		Aluno aluno = novoAlunoBasico("nota.invalida@example.com");
		ServicoDeCursos servico = new ServicoDeCursos();
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> servico.finalizarCurso(aluno, 11.0));

		assertEquals("Nota deve estar entre 0 e 10", exception.getMessage());
	}

	private Aluno novoAlunoBasico(String email) {
		return Aluno.novo("Aluno Básico", ContatoEmail.of(email), TipoAssinatura.ASSINATURA_BASICA);
	}
}
