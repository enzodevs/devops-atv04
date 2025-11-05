package br.com.facens.atividade4.application.dto;

import br.com.facens.atividade4.domain.ContatoEmail;
import br.com.facens.atividade4.domain.TipoAssinatura;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AtualizarAlunoDTO {
    @NotBlank
    @Size(max = 120)
    private String nome;

    @NotBlank
    @Email
    @Size(max = 120)
    private String email;

    @NotNull
    private TipoAssinatura assinatura;

    public ContatoEmail contatoEmail() {
        return ContatoEmail.of(email);
    }
}
