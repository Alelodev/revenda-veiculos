package br.com.revenda.service;

import br.com.revenda.exception.DadosInvalidosException;
import br.com.revenda.exception.RecursoNaoEncontradoException;
import br.com.revenda.model.Proprietario;
import br.com.revenda.model.Usuario;
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

        String placaNormalizada =
                veiculo.getPlaca()
                        .trim()
                        .toUpperCase();

        veiculo.setPlaca(placaNormalizada);

        if (veiculoRepository.existsByPlaca(veiculo.getPlaca())) {
            throw new DadosInvalidosException(
                    "Já existe um veículo cadastrado com essa placa."
            );
        }

        if (veiculo.getProprietario() != null) {

            Proprietario proprietarioExistente =
                    proprietarioService.buscarId(
                            veiculo.getProprietario().getIdProprietario()
                    );

            veiculo.setProprietario(proprietarioExistente);

            return veiculoRepository.save(veiculo);
        }

        throw new DadosInvalidosException(
                "Proprietário é obrigatório"
        );
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

    public List<Veiculo> buscarPorUsuario(Long idUsuario) {
        return veiculoRepository.findByUsuarioIdUsuario(idUsuario);
    }

    public Veiculo buscarVeiculoPermitido(Long idVeiculo, Usuario usuario) {

        Veiculo veiculo = buscarId(idVeiculo);

        if (usuario.getPerfil().equals("ADMIN")) {
            return veiculo;
        }

        if (veiculo.getUsuario() == null ||
                !veiculo.getUsuario().getIdUsuario().equals(usuario.getIdUsuario())) {

            throw new RecursoNaoEncontradoException(
                    "Veículo não encontrado"
            );
        }

        return veiculo;
    }
}
