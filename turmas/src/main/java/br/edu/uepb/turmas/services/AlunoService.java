package br.edu.uepb.turmas.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javassist.NotFoundException;
import br.edu.uepb.turmas.domain.Aluno;
import br.edu.uepb.turmas.domain.IntegranteENUM;
import br.edu.uepb.turmas.domain.Projeto;
import br.edu.uepb.turmas.domain.User;
import br.edu.uepb.turmas.exceptions.DadosIguaisException;
import br.edu.uepb.turmas.repository.AlunoRepository;
import br.edu.uepb.turmas.repository.ProjetoRepository;
import br.edu.uepb.turmas.repository.UserRepository;

@Service
public class AlunoService {
    @Autowired
    private AlunoRepository alunoRepository;
    @Autowired
    private ProjetoRepository projetoRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public boolean verificarPorId(Long id) {
        return alunoRepository.existsById(id);
    }

    public boolean verificarPorEmail(String email) {
        return alunoRepository.existsByemail(email);
    }

    public List<Aluno> listAllAlunos() {
        return alunoRepository.findAll();
    }

    public Aluno criarAlunos( Aluno aluno) throws DadosIguaisException {
        User user=aluno;
        if ((verificarPorId(aluno.getId())) || (verificarPorEmail(aluno.getEmail())))
            throw new DadosIguaisException("Já existe um Aluno com essa matricula ou e-mail");
        else if(userRepository.findByUsername(user.getUsername()) != null){
            throw new DadosIguaisException("Já existe um usuário com esse username");
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setUsername(user.getUsername());
        user.setAuthority("ROLE_ALUNO");
        return alunoRepository.save(aluno);

    }

    public Aluno atualizarAlunos(Long id, Aluno aluno) throws NotFoundException, DadosIguaisException {
        User user=aluno;
        try {
            if ((alunoRepository.findById(id).get()) == null) {
                throw new NotFoundException("Não foi encontrado nenhum Aluno com essa matricula: " + id);

            }else if(userRepository.findByUsername(user.getUsername()) != null){
                throw new DadosIguaisException("Já existe um usuário com esse username");
            }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setUsername(user.getUsername());
        user.setAuthority("ROLE_ALUNO");
            return alunoRepository.save(aluno);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Não foi encontrado nenhum Aluno com essa matricula: " + id);
        }

    }

    public Aluno findById(Long id) throws NotFoundException {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Não foi encontrado nenhum Aluno com essa matricula!"));
    }

    public void apagarAlunos(Long id) throws NotFoundException {
        try {
            alunoRepository.delete(alunoRepository.findById(id).get());
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Não foi encontrado nenhum Aluno com essa matricula: " + id);
        }
    }

    public Aluno vincularProjetoAluno(Long alunoId, Long projetoId, String papel) throws NotFoundException {
        try {
            if (IntegranteENUM.validar(papel)) {
                ;
                Aluno aluno = alunoRepository.findById(alunoId).get();
                Projeto projeto = projetoRepository.findById(projetoId).get();
                aluno.setProjeto(projeto);
                aluno.setFuncao(IntegranteENUM.valueOf(papel));
                return alunoRepository.save(aluno);
            } else {
                throw new NoSuchElementException("Papel Inválido ou não encontrado");
            }
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Não foi encontrado, o identificador informado");

        }

    }
}
