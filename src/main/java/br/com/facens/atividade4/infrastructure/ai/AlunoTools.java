package br.com.facens.atividade4.infrastructure.ai;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.com.facens.atividade4.domain.Aluno;
import br.com.facens.atividade4.infrastructure.persistence.AlunoRepository;
import lombok.RequiredArgsConstructor;

/**
 * Ferramentas que o AI Course Advisor pode usar para buscar informações reais do sistema.
 * Fornece contexto do banco de dados para enriquecer as respostas da IA.
 */
@Component
@RequiredArgsConstructor
public class AlunoTools {

    private final AlunoRepository alunoRepository;

    public String buscarInformacoesAluno(Long alunoId) {
        Optional<Aluno> alunoOpt = alunoRepository.findById(alunoId);

        if (alunoOpt.isEmpty()) {
            return String.format("Aluno com ID %d não encontrado no sistema.", alunoId);
        }

        Aluno aluno = alunoOpt.get();
        return String.format(
            "Aluno: %s (ID: %d)\n" +
            "Email: %s\n" +
            "Tipo de Assinatura: %s\n" +
            "Cursos Disponíveis: %d",
            aluno.getNome(),
            aluno.getId(),
            aluno.getContatoEmail().getEndereco(),
            aluno.getTipoAssinatura(),
            aluno.getCursosDisponiveis()
        );
    }

    public String listarTodosAlunos() {
        List<Aluno> alunos = alunoRepository.findAll();

        if (alunos.isEmpty()) {
            return "Nenhum aluno cadastrado no sistema ainda.";
        }

        return alunos.stream()
            .map(a -> String.format("ID: %d - %s (%s) - %d cursos",
                a.getId(),
                a.getNome(),
                a.getTipoAssinatura(),
                a.getCursosDisponiveis()))
            .collect(Collectors.joining("\n"));
    }

    public String explicarTiposAssinatura() {
        return "Tipos de Assinatura Disponíveis:\n\n" +
               "1. ASSINATURA_BASICA:\n" +
               "   - Cursos iniciais: 5\n" +
               "   - Ideal para iniciantes que querem experimentar a plataforma\n\n" +
               "2. ASSINATURA_PREMIUM:\n" +
               "   - Cursos iniciais: 10\n" +
               "   - Mais cursos disponíveis desde o início\n" +
               "   - Melhor custo-benefício para estudantes dedicados";
    }

    public String explicarGamificacao() {
        return "Sistema de Gamificação:\n\n" +
               "Quando um aluno finaliza um curso, ele pode ganhar cursos bônus baseado em:\n" +
               "- Nota obtida no curso (quanto maior, melhor)\n" +
               "- Tipo de assinatura (Premium pode ter benefícios adicionais)\n\n" +
               "Os cursos bônus são adicionados automaticamente ao saldo de cursos disponíveis,\n" +
               "permitindo que o aluno continue estudando e evoluindo na plataforma.";
    }
}
