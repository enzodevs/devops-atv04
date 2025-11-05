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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/alunos")
public class AlunoController {

    private final AlunoApplicationService alunoService;

    @GetMapping
    public List<AlunoDTO> listarTodos() {
        return alunoService.listarTodos();
    }

    @GetMapping("/{id}")
    public AlunoDTO buscarPorId(@PathVariable Long id) {
        return alunoService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<AlunoDTO> criar(@Valid @RequestBody CriarAlunoDTO dto) {
        AlunoDTO alunoCriado = alunoService.criar(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(alunoCriado.getId())
                .toUri();
        return ResponseEntity.created(location).body(alunoCriado);
    }

    @PutMapping("/{id}")
    public AlunoDTO atualizar(@PathVariable Long id, @Valid @RequestBody AtualizarAlunoDTO dto) {
        return alunoService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        alunoService.remover(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/finalizar-curso")
    public AlunoDTO finalizarCurso(@PathVariable Long id, @Valid @RequestBody FinalizarCursoRequest request) {
        return alunoService.finalizarCurso(id, request.nota());
    }
}
