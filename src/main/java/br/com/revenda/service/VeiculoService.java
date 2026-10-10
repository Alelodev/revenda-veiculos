package br.com.revenda.service;

import br.com.revenda.exception.DadosInvalidosException;
import br.com.revenda.exception.RecursoNaoEncontradoException;
import br.com.revenda.model.Proprietario;
import br.com.revenda.model.Veiculo;
import br.com.revenda.repository.VeiculoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
            Proprietario proprietarioExistente =
                    proprietarioService.buscarId(
                            veiculo.getProprietario().getIdProprietario()
                    );

            veiculo.setProprietario(proprietarioExistente);
            return veiculoRepository.save(veiculo);

        } else {
            throw new DadosInvalidosException("Proprietário é obrigatório");
        }
    }

    public List<Veiculo> mostrarTodos() {
        return veiculoRepository.findAll();
    }

    public Veiculo buscarId(Long id) {
        return veiculoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Veiculo nao encontrado")
                );
    }

    public void deletarId(Long id) {
        if (veiculoRepository.existsById(id)) {
            veiculoRepository.deleteById(id);
        } else {
            throw new RecursoNaoEncontradoException("Veiculo nao encontrado");
        }

    }

    public Veiculo atualizar(Long id, Veiculo novosDados) {

        Veiculo veiculo = veiculoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Veículo não encontrado")
                );

        if (novosDados.getProprietario() == null) {
            throw new DadosInvalidosException("Proprietário é obrigatório");
        }

        Proprietario proprietarioExistente =
                proprietarioService.buscarId(
                        novosDados.getProprietario().getIdProprietario()
                );

        veiculo.setProprietario(proprietarioExistente);
        veiculo.setAnoFabricacao(novosDados.getAnoFabricacao());
        veiculo.setAnoModelo(novosDados.getAnoModelo());
        veiculo.setChassi(novosDados.getChassi());
        veiculo.setCor(novosDados.getCor());
        veiculo.setMarca(novosDados.getMarca());
        veiculo.setPlaca(novosDados.getPlaca());
        veiculo.setModelo(novosDados.getModelo());
        veiculo.setTipoCombustivel(novosDados.getTipoCombustivel());
        veiculo.setRenavam(novosDados.getRenavam());
        veiculo.setQuilometragem(novosDados.getQuilometragem());
        veiculo.setValorCompra(novosDados.getValorCompra());
        veiculo.setValorVenda(novosDados.getValorVenda());
        veiculo.setStatus(novosDados.getStatus());
        return veiculoRepository.save(veiculo);
    }
}
