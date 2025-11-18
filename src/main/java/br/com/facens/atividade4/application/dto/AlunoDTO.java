package br.com.facens.atividade4.application.dto;

import br.com.facens.atividade4.domain.Aluno;
import br.com.facens.atividade4.domain.TipoAssinatura;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "Representação completa de um aluno no sistema")
public class AlunoDTO {
    @Schema(description = "Identificador único do aluno", example = "1")
    private Long id;

    @Schema(description = "Nome completo do aluno", example = "João Silva", minLength = 3, maxLength = 120)
    private String nome;

    @Schema(description = "E-mail do aluno", example = "joao.silva@email.com", format = "email")
    private String email;

    @Schema(description = "Tipo de assinatura do aluno", example = "PRO")
    private TipoAssinatura assinatura;

    @Schema(description = "Quantidade de cursos disponíveis para o aluno", example = "10", minimum = "0")
    private int cursosDisponiveis;

    public static AlunoDTO fromEntity(Aluno aluno) {
        return AlunoDTO.builder()
                .id(aluno.getId())
                .nome(aluno.getNome())
                .email(aluno.getContatoEmail().getEndereco())
                .assinatura(aluno.getTipoAssinatura())
                .cursosDisponiveis(aluno.getCursosDisponiveis())
                .build();
    }
}
