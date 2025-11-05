package br.com.facens.atividade4.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.facens.atividade4.domain.Aluno;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    boolean existsByContatoEmailEndereco(String endereco);

    Optional<Aluno> findByContatoEmailEndereco(String endereco);
}
