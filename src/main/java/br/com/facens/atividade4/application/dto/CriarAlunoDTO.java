package br.com.facens.atividade4.application.dto;

import br.com.facens.atividade4.domain.ContatoEmail;
import br.com.facens.atividade4.domain.TipoAssinatura;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Dados necessários para criar um novo aluno")
public class CriarAlunoDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 120, message = "Nome deve ter no máximo 120 caracteres")
    @Schema(description = "Nome completo do aluno", example = "Maria Santos", required = true, maxLength = 120)
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    @Size(max = 120, message = "Email deve ter no máximo 120 caracteres")
    @Schema(description = "E-mail do aluno", example = "maria.santos@email.com", required = true, format = "email", maxLength = 120)
    private String email;

    @NotNull(message = "Tipo de assinatura é obrigatório")
    @Schema(description = "Tipo de assinatura do aluno", example = "FREE", required = true, enumAsRef = true)
    private TipoAssinatura assinatura;

    public ContatoEmail contatoEmail() {
        return ContatoEmail.of(email);
    }
}
