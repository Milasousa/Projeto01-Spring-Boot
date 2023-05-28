package br.edu.uepb.turmas.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.uepb.turmas.domain.Aluno;
import br.edu.uepb.turmas.domain.Professor;
import br.edu.uepb.turmas.domain.Turma;
import br.edu.uepb.turmas.repository.AlunoRepository;
import br.edu.uepb.turmas.repository.ProfessorRepository;
import br.edu.uepb.turmas.repository.TurmaRepository;
import javassist.NotFoundException;

@Service
public class TurmaService {
    @Autowired
    private TurmaRepository turmarepository;
    @Autowired
    private AlunoRepository alunorepository;
    @Autowired
    private ProfessorRepository professorrepository;

    public List<Turma> listAllTurmas() {
        return turmarepository.findAll();
    }

    public Turma criarTurma(Turma turma) {
        return turmarepository.save(turma);
    }

    public Turma atualizarTurma(Long id, Turma turma) throws NotFoundException {
        try {
            if ((turmarepository.findById(id).get()) == null) {
                throw new NotFoundException("Não existe nenhuma Turma com esse identificador: " + id);

            }
            return turmarepository.save(turma);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Não existe nenhuma Turma com esse identificador: " + id);

        }
    }

    public void apagarTurma(Long id) throws NotFoundException {
        try {
            turmarepository.delete(turmarepository.findById(id).get());
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Não existe nenhuma Turma com esse identificador: " + id);

        }
    }

    public Turma vincularTurmaAluno(Long turmaId, Long alunoId) throws NotFoundException {
        try {
            Aluno aluno = alunorepository.findById(alunoId).get();
            Turma turma = turmarepository.findById(turmaId).get();
            turma.setAluno(aluno);
            return turmarepository.save(turma);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Não foi encontrado, o identificador informado");

        }

    }

    public Turma vincularTurmaProfessor(Long turmaId, Long profId) throws NotFoundException {
        try {
            Professor prof = professorrepository.findById(profId).get();
            Turma turma = turmarepository.findById(turmaId).get();
            turma.setProfessor(prof);
            return turmarepository.save(turma);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Não foi encontrado, o identificador informado");

        }
    }

    public Turma vincularTurma(Long turmaId, Long alunoId, Long profId) throws NotFoundException {
        try {
            Professor prof = professorrepository.findById(profId).get();
            Aluno aluno = alunorepository.findById(alunoId).get();
            Turma turma = turmarepository.findById(turmaId).get();
            turma.setAluno(aluno);
            turma.setProfessor(prof);
            return turmarepository.save(turma);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Não foi encontrado, o identificador informado");

        }
    }

    public Turma findById(Long id) throws NotFoundException {
        return turmarepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Não existe nenhuma Turma com esse identificador: " + id));
    }

}
