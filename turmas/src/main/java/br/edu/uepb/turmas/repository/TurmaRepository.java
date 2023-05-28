package br.edu.uepb.turmas.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.edu.uepb.turmas.domain.Turma;
@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long> {}