package br.com.revenda.service;

import br.com.revenda.model.Proprietario;
import br.com.revenda.repository.ProprietarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProprietarioService {

    private ProprietarioRepository proprietarioRepository;

    public ProprietarioService(ProprietarioRepository proprietarioRepository) {
        this.proprietarioRepository = proprietarioRepository;
    }

    public Proprietario salvar(Proprietario proprietario) {
        return proprietarioRepository.save(proprietario);
    }

    public List<Proprietario> mostrarTodos(){
        return proprietarioRepository.findAll();
    }

    public Optional<Proprietario> buscarId(Long id){
        return proprietarioRepository.findById(id);
    }

    public void deletarId(Long id){
        proprietarioRepository.deleteById(id);
    }
}
