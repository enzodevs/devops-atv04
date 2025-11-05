package br.com.facens.atividade4.application.dto;

import br.com.facens.atividade4.domain.Aluno;
import br.com.facens.atividade4.domain.TipoAssinatura;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlunoDTO {
    private Long id;

    private String nome;

    private String email;

    private TipoAssinatura assinatura;

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
