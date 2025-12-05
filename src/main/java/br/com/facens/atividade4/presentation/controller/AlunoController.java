package br.com.facens.atividade4.presentation.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.facens.atividade4.application.dto.AlunoDTO;
import br.com.facens.atividade4.application.dto.AtualizarAlunoDTO;
import br.com.facens.atividade4.application.dto.CriarAlunoDTO;
import br.com.facens.atividade4.application.service.AlunoApplicationService;
import br.com.facens.atividade4.presentation.request.FinalizarCursoRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/alunos")
@Tag(name = "Alunos", description = "Gerenciamento de alunos com gamificação")
public class AlunoController {

    // Controller REST que expõe CRUD de alunos e finalização de curso com bônus e mapeia respostas HTTP.
    private final AlunoApplicationService alunoService;

    @GetMapping
    @Operation(summary = "Listar todos os alunos", description = "Retorna uma lista com todos os alunos cadastrados no sistema")
    @ApiResponse(responseCode = "200", description = "Lista de alunos retornada com sucesso")
    public List<AlunoDTO> listarTodos() {
        return alunoService.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar aluno por ID", description = "Retorna os dados de um aluno específico pelo seu ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Aluno encontrado"),
        @ApiResponse(responseCode = "404", description = "Aluno não encontrado", content = @Content)
    })
    public AlunoDTO buscarPorId(@Parameter(description = "ID do aluno", required = true) @PathVariable Long id) {
        return alunoService.buscarPorId(id);
    }

    @PostMapping
    @Operation(summary = "Criar novo aluno", description = "Cadastra um novo aluno no sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Aluno criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
        @ApiResponse(responseCode = "409", description = "Email já cadastrado", content = @Content)
    })
    public ResponseEntity<AlunoDTO> criar(@Valid @RequestBody CriarAlunoDTO dto) {
        AlunoDTO alunoCriado = alunoService.criar(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(alunoCriado.getId())
                .toUri();
        return ResponseEntity.created(location).body(alunoCriado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar aluno", description = "Atualiza os dados de um aluno existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Aluno atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
        @ApiResponse(responseCode = "404", description = "Aluno não encontrado", content = @Content),
        @ApiResponse(responseCode = "409", description = "Email já cadastrado por outro aluno", content = @Content)
    })
    public AlunoDTO atualizar(
            @Parameter(description = "ID do aluno", required = true) @PathVariable Long id,
            @Valid @RequestBody AtualizarAlunoDTO dto) {
        return alunoService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover aluno", description = "Remove um aluno do sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Aluno removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Aluno não encontrado", content = @Content)
    })
    public ResponseEntity<Void> remover(@Parameter(description = "ID do aluno", required = true) @PathVariable Long id) {
        alunoService.remover(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/finalizar-curso")
    @Operation(summary = "Finalizar curso", description = "Finaliza um curso e aplica bônus de gamificação baseado na nota e tipo de assinatura")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Curso finalizado e bônus aplicado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
        @ApiResponse(responseCode = "404", description = "Aluno não encontrado", content = @Content)
    })
    public AlunoDTO finalizarCurso(
            @Parameter(description = "ID do aluno", required = true) @PathVariable Long id,
            @Valid @RequestBody FinalizarCursoRequest request) {
        return alunoService.finalizarCurso(id, request.nota());
    }
}
