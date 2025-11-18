package br.com.facens.atividade4.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Requisição para finalizar um curso e aplicar bônus de gamificação")
public record FinalizarCursoRequest(
        @NotNull(message = "Nota é obrigatória")
        @DecimalMin(value = "0.0", message = "Nota deve ser no mínimo 0.0")
        @DecimalMax(value = "10.0", message = "Nota deve ser no máximo 10.0")
        @Schema(description = "Nota obtida pelo aluno no curso", example = "8.5", minimum = "0.0", maximum = "10.0", required = true)
        Double nota
) {
}
