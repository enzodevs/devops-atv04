package br.com.facens.atividade4.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.facens.atividade4.domain.Aluno;
import br.com.facens.atividade4.domain.ContatoEmail;
import br.com.facens.atividade4.domain.TipoAssinatura;

@DataJpaTest
@ActiveProfiles("test")
class AlunoRepositoryTest {

    // Testa persistência JPA e consultas de email no repositório de alunos (perfil test, H2).
    @Autowired
    private AlunoRepository repository;

    @Test
    void devePersistirAlunoComSucesso() {
        Aluno aluno = Aluno.novo("João Teste", ContatoEmail.of("joao.teste@example.com"),
                TipoAssinatura.ASSINATURA_BASICA);

        Aluno salvo = repository.save(aluno);

        assertNotNull(salvo.getId());
        assertEquals("joao.teste@example.com", salvo.getContatoEmail().getEndereco());
        assertEquals(TipoAssinatura.ASSINATURA_BASICA, salvo.getTipoAssinatura());
        assertEquals(TipoAssinatura.ASSINATURA_BASICA.getCursosIniciais(), salvo.getCursosDisponiveis());
    }

    @Test
    void deveDetectarEmailExistente() {
        repository.save(Aluno.novo("Maria Email", ContatoEmail.of("maria.email@example.com"),
                TipoAssinatura.ASSINATURA_PREMIUM));

        assertTrue(repository.existsByContatoEmailEndereco("maria.email@example.com"));
    }
}
