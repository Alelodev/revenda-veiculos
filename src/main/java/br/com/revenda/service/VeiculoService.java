package br.com.revenda.service;

import br.com.revenda.model.Veiculo;
import br.com.revenda.repository.VeiculoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VeiculoService {

    private VeiculoRepository veiculoRepository;

    public VeiculoService(VeiculoRepository veiculoRepository) {
        this.veiculoRepository = veiculoRepository;
    }

    public Veiculo salvar(Veiculo veiculo) {
        return veiculoRepository.save(veiculo);
    }

    public List<Veiculo> mostrarTodos() {
        return veiculoRepository.findAll();
    }

    public Optional<Veiculo> buscarId(Long id) {
        return veiculoRepository.findById(id);
    }

    public void deletarId(Long id) {
        veiculoRepository.deleteById(id);
    }
}
