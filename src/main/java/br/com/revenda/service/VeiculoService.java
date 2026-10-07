package br.com.revenda.service;

import br.com.revenda.model.Proprietario;
import br.com.revenda.model.Veiculo;
import br.com.revenda.repository.VeiculoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VeiculoService {

    private VeiculoRepository veiculoRepository;
    private ProprietarioService proprietarioService;

    public VeiculoService(VeiculoRepository veiculoRepository, ProprietarioService proprietarioService) {
        this.veiculoRepository = veiculoRepository;
        this.proprietarioService = proprietarioService;
    }

    public Veiculo salvar(Veiculo veiculo) {
        if (veiculo.getProprietario() != null) {
            Optional<Proprietario> proprietarioExistente =
                    proprietarioService.buscarId(
                            veiculo.getProprietario().getIdProprietario()
                    );
            if(proprietarioExistente.isPresent()){
                veiculo.setProprietario(proprietarioExistente.get());
                return veiculoRepository.save(veiculo);
            } else{
                return null;
            }
        }else {
            return null;
        }
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
