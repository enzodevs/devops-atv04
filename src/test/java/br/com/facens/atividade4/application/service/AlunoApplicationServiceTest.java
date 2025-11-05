package br.com.facens.atividade4.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.facens.atividade4.application.dto.AlunoDTO;
import br.com.facens.atividade4.application.dto.AtualizarAlunoDTO;
import br.com.facens.atividade4.application.dto.CriarAlunoDTO;
import br.com.facens.atividade4.application.exception.AlunoNaoEncontradoException;
import br.com.facens.atividade4.application.exception.EmailAlunoJaCadastradoException;
import br.com.facens.atividade4.domain.Aluno;
import br.com.facens.atividade4.domain.ContatoEmail;
import br.com.facens.atividade4.domain.TipoAssinatura;
import br.com.facens.atividade4.infrastructure.persistence.AlunoRepository;
import br.com.facens.atividade4.service.ServicoDeCursos;

@ExtendWith(MockitoExtension.class)
class AlunoApplicationServiceTest {

    @Mock
    private AlunoRepository alunoRepository;

    @Mock
    private ServicoDeCursos servicoDeCursos;

    @InjectMocks
    private AlunoApplicationService alunoService;

    private CriarAlunoDTO criarAlunoDTO;

    @BeforeEach
    void setUp() {
        criarAlunoDTO = new CriarAlunoDTO();
        criarAlunoDTO.setNome("João Cursos");
        criarAlunoDTO.setEmail("joao.cursos@example.com");
        criarAlunoDTO.setAssinatura(TipoAssinatura.ASSINATURA_BASICA);
    }

    @Test
    void deveCriarAlunoQuandoEmailDisponivel() throws Exception {
        when(alunoRepository.findByContatoEmailEndereco("joao.cursos@example.com")).thenReturn(Optional.empty());
        when(alunoRepository.save(any(Aluno.class))).thenAnswer(invocation -> atribuirId(invocation.getArgument(0), 1L));

        AlunoDTO resultado = alunoService.criar(criarAlunoDTO);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNome()).isEqualTo("João Cursos");
        assertThat(resultado.getEmail()).isEqualTo("joao.cursos@example.com");
        verify(alunoRepository).save(any(Aluno.class));
    }

    @Test
    void deveListarAlunos() {
        Aluno aluno = Aluno.builder()
                .id(1L)
                .nome("Maria Cursos")
                .contatoEmail(ContatoEmail.of("maria.cursos@example.com"))
                .tipoAssinatura(TipoAssinatura.ASSINATURA_PREMIUM)
                .cursosDisponiveis(10)
                .build();

        when(alunoRepository.findAll()).thenReturn(List.of(aluno));

        List<AlunoDTO> alunos = alunoService.listarTodos();

        assertThat(alunos).hasSize(1);
        assertThat(alunos.get(0).getEmail()).isEqualTo("maria.cursos@example.com");
    }

    @Test
    void deveLancarErroAoCriarAlunoComEmailDuplicado() {
        when(alunoRepository.findByContatoEmailEndereco("joao.cursos@example.com"))
                .thenReturn(Optional.of(Aluno.builder()
                        .id(99L)
                        .nome("Outro")
                        .contatoEmail(ContatoEmail.of("joao.cursos@example.com"))
                        .tipoAssinatura(TipoAssinatura.ASSINATURA_PREMIUM)
                        .cursosDisponiveis(10)
                        .build()));

        assertThrows(EmailAlunoJaCadastradoException.class, () -> alunoService.criar(criarAlunoDTO));
    }

    @Test
    void deveAtualizarAluno() {
        Aluno existente = Aluno.builder()
                .id(2L)
                .nome("Aluno Antigo")
                .contatoEmail(ContatoEmail.of("antigo@example.com"))
                .tipoAssinatura(TipoAssinatura.ASSINATURA_BASICA)
                .cursosDisponiveis(5)
                .build();

        AtualizarAlunoDTO atualizarAlunoDTO = new AtualizarAlunoDTO();
        atualizarAlunoDTO.setNome("Aluno Atualizado");
        atualizarAlunoDTO.setEmail("novo.email@example.com");
        atualizarAlunoDTO.setAssinatura(TipoAssinatura.ASSINATURA_PREMIUM);

        when(alunoRepository.findById(2L)).thenReturn(Optional.of(existente));
        when(alunoRepository.findByContatoEmailEndereco("novo.email@example.com")).thenReturn(Optional.empty());

        AlunoDTO atualizado = alunoService.atualizar(2L, atualizarAlunoDTO);

        assertThat(atualizado.getNome()).isEqualTo("Aluno Atualizado");
        assertThat(atualizado.getAssinatura()).isEqualTo(TipoAssinatura.ASSINATURA_PREMIUM);
        assertThat(atualizado.getEmail()).isEqualTo("novo.email@example.com");
    }

    @Test
    void deveAtualizarAlunoSemAlterarEmailQuandoIgualIgnorandoCase() {
        Aluno existente = Aluno.builder()
                .id(20L)
                .nome("Aluno Antigo")
                .contatoEmail(ContatoEmail.of("MESMO@EXAMPLE.COM"))
                .tipoAssinatura(TipoAssinatura.ASSINATURA_BASICA)
                .cursosDisponiveis(5)
                .build();

        AtualizarAlunoDTO atualizarAlunoDTO = new AtualizarAlunoDTO();
        atualizarAlunoDTO.setNome("Aluno Atualizado");
        atualizarAlunoDTO.setEmail("mesmo@example.com");
        atualizarAlunoDTO.setAssinatura(TipoAssinatura.ASSINATURA_PREMIUM);

        when(alunoRepository.findById(20L)).thenReturn(Optional.of(existente));

        AlunoDTO atualizado = alunoService.atualizar(20L, atualizarAlunoDTO);

        assertThat(atualizado.getEmail()).isEqualTo("mesmo@example.com");
        verify(alunoRepository).findById(20L);
    }

    @Test
    void deveLancarErroAoAtualizarComEmailDeOutroAluno() {
        Aluno existente = Aluno.builder()
                .id(3L)
                .nome("Aluno")
                .contatoEmail(ContatoEmail.of("aluno@example.com"))
                .tipoAssinatura(TipoAssinatura.ASSINATURA_BASICA)
                .cursosDisponiveis(5)
                .build();

        Aluno outro = Aluno.builder()
                .id(10L)
                .nome("Outro")
                .contatoEmail(ContatoEmail.of("duplicado@example.com"))
                .tipoAssinatura(TipoAssinatura.ASSINATURA_PREMIUM)
                .cursosDisponiveis(10)
                .build();

        AtualizarAlunoDTO atualizarAlunoDTO = new AtualizarAlunoDTO();
        atualizarAlunoDTO.setNome("Aluno");
        atualizarAlunoDTO.setEmail("duplicado@example.com");
        atualizarAlunoDTO.setAssinatura(TipoAssinatura.ASSINATURA_PREMIUM);

        when(alunoRepository.findById(3L)).thenReturn(Optional.of(existente));
        when(alunoRepository.findByContatoEmailEndereco("duplicado@example.com")).thenReturn(Optional.of(outro));

        assertThrows(EmailAlunoJaCadastradoException.class, () -> alunoService.atualizar(3L, atualizarAlunoDTO));
    }

    @Test
    void naoDeveReutilizarEmailQuandoPertencerAoMesmoAluno() {
        Aluno existente = Aluno.builder()
                .id(5L)
                .nome("Mesmo Aluno")
                .contatoEmail(ContatoEmail.of("mesmo@example.com"))
                .tipoAssinatura(TipoAssinatura.ASSINATURA_PREMIUM)
                .cursosDisponiveis(10)
                .build();

        AtualizarAlunoDTO dto = new AtualizarAlunoDTO();
        dto.setNome("Mesmo Aluno");
        dto.setEmail("novo.email@example.com");
        dto.setAssinatura(TipoAssinatura.ASSINATURA_PREMIUM);

        when(alunoRepository.findById(5L)).thenReturn(Optional.of(existente));
        when(alunoRepository.findByContatoEmailEndereco("novo.email@example.com"))
                .thenReturn(Optional.of(existente));

        AlunoDTO atualizado = alunoService.atualizar(5L, dto);

        assertThat(atualizado.getEmail()).isEqualTo("novo.email@example.com");
        verify(alunoRepository).findByContatoEmailEndereco("novo.email@example.com");
    }

    @Test
    void naoDeveReutilizarEmailQuandoPertencerAoMesmoAlunoComMesmoEmailIgnorandoCase() {
        Aluno existente = Aluno.builder()
                .id(6L)
                .nome("Mesmo Aluno")
                .contatoEmail(ContatoEmail.of("mesmo@example.com"))
                .tipoAssinatura(TipoAssinatura.ASSINATURA_PREMIUM)
                .cursosDisponiveis(10)
                .build();

        AtualizarAlunoDTO dto = new AtualizarAlunoDTO();
        dto.setNome("Mesmo Aluno");
        dto.setEmail("MESMO@example.com");
        dto.setAssinatura(TipoAssinatura.ASSINATURA_PREMIUM);

        when(alunoRepository.findById(6L)).thenReturn(Optional.of(existente));
        AlunoDTO atualizado = alunoService.atualizar(6L, dto);

        assertThat(atualizado.getEmail()).isEqualTo("mesmo@example.com");
        verify(alunoRepository, never()).findByContatoEmailEndereco("mesmo@example.com");
    }

    @Test
    void deveRemoverAluno() {
        Aluno existente = Aluno.builder()
                .id(4L)
                .nome("Aluno Removido")
                .contatoEmail(ContatoEmail.of("remover@example.com"))
                .tipoAssinatura(TipoAssinatura.ASSINATURA_BASICA)
                .cursosDisponiveis(5)
                .build();

        when(alunoRepository.findById(4L)).thenReturn(Optional.of(existente));
        doNothing().when(alunoRepository).delete(existente);

        alunoService.remover(4L);

        verify(alunoRepository, times(1)).delete(existente);
    }

    @Test
    void deveFinalizarCurso() {
        Aluno existente = Aluno.builder()
                .id(5L)
                .nome("Aluno Curso")
                .contatoEmail(ContatoEmail.of("curso@example.com"))
                .tipoAssinatura(TipoAssinatura.ASSINATURA_BASICA)
                .cursosDisponiveis(5)
                .build();

        when(alunoRepository.findById(5L)).thenReturn(Optional.of(existente));

        alunoService.finalizarCurso(5L, 8.0);

        verify(servicoDeCursos).finalizarCurso(existente, 8.0);
    }

    @Test
    void deveLancarErroQuandoAlunoNaoExistir() {
        when(alunoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(AlunoNaoEncontradoException.class, () -> alunoService.buscarPorId(99L));
    }

    @Test
    void deveBuscarAlunoPorIdComSucesso() {
        Aluno existente = Aluno.builder()
                .id(8L)
                .nome("Aluno Registro")
                .contatoEmail(ContatoEmail.of("registro@example.com"))
                .tipoAssinatura(TipoAssinatura.ASSINATURA_BASICA)
                .cursosDisponiveis(5)
                .build();

        when(alunoRepository.findById(8L)).thenReturn(Optional.of(existente));

        AlunoDTO dto = alunoService.buscarPorId(8L);

        assertThat(dto.getId()).isEqualTo(8L);
        assertThat(dto.getEmail()).isEqualTo("registro@example.com");
        verify(alunoRepository).findById(8L);
    }

    private Aluno atribuirId(Aluno aluno, Long id) {
        try {
            Field idField = Aluno.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(aluno, id);
            return aluno;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
