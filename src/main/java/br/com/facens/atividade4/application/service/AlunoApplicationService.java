package br.com.facens.atividade4.application.service;

import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.facens.atividade4.application.dto.AlunoDTO;
import br.com.facens.atividade4.application.dto.AtualizarAlunoDTO;
import br.com.facens.atividade4.application.dto.CriarAlunoDTO;
import br.com.facens.atividade4.application.exception.AlunoNaoEncontradoException;
import br.com.facens.atividade4.application.exception.EmailAlunoJaCadastradoException;
import br.com.facens.atividade4.domain.Aluno;
import br.com.facens.atividade4.infrastructure.persistence.AlunoRepository;
import br.com.facens.atividade4.service.ServicoDeCursos;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AlunoApplicationService {

    // Orquestra casos de uso: cria/atualiza/remover/finaliza curso; valida email único; aciona gamificação.
    private final AlunoRepository alunoRepository;
    private final ServicoDeCursos servicoDeCursos;

    @Transactional(readOnly = true)
    public List<AlunoDTO> listarTodos() {
        return alunoRepository.findAll()
                .stream()
                .map(AlunoDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public AlunoDTO buscarPorId(Long id) {
        return AlunoDTO.fromEntity(buscarAlunoOuErro(id));
    }

    public AlunoDTO criar(CriarAlunoDTO dto) {
        validarEmailDisponivel(dto.getEmail(), null);

        Aluno aluno = Aluno.novo(dto.getNome(), dto.contatoEmail(), dto.getAssinatura());
        alunoRepository.save(aluno);
        return AlunoDTO.fromEntity(aluno);
    }

    public AlunoDTO atualizar(Long id, AtualizarAlunoDTO dto) {
        Aluno aluno = buscarAlunoOuErro(id);
        if (!aluno.getContatoEmail().getEndereco().equalsIgnoreCase(dto.getEmail())) {
            validarEmailDisponivel(dto.getEmail(), id);
        }

        aluno.atualizarDados(dto.getNome(), dto.contatoEmail(), dto.getAssinatura());
        return AlunoDTO.fromEntity(aluno);
    }

    public void remover(Long id) {
        Aluno aluno = buscarAlunoOuErro(id);
        alunoRepository.delete(aluno);
    }

    public AlunoDTO finalizarCurso(Long id, double nota) {
        Aluno aluno = buscarAlunoOuErro(id);
        servicoDeCursos.finalizarCurso(aluno, nota);
        return AlunoDTO.fromEntity(aluno);
    }

    private Aluno buscarAlunoOuErro(Long id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new AlunoNaoEncontradoException(id));
    }

    private void validarEmailDisponivel(String email, Long idAtual) {
        String emailNormalizado = email.toLowerCase(Locale.ROOT);
        boolean existeEmail = alunoRepository.findByContatoEmailEndereco(emailNormalizado)
                .filter(alunoExistente -> idAtual == null || !alunoExistente.getId().equals(idAtual))
                .isPresent();

        if (existeEmail) {
            throw new EmailAlunoJaCadastradoException(email);
        }
    }
}
