package br.com.revenda.service;

import br.com.revenda.exception.DadosInvalidosException;
import br.com.revenda.exception.RecursoNaoEncontradoException;
import br.com.revenda.model.Documento;
import br.com.revenda.model.Veiculo;
import br.com.revenda.repository.DocumentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentoService {

    private DocumentoRepository documentoRepository;
    private VeiculoService veiculoService;

    public DocumentoService(DocumentoRepository documentoRepository, VeiculoService veiculoService) {
        this.documentoRepository = documentoRepository;
        this.veiculoService = veiculoService;
    }

    public Documento salvar(Documento documento) {
        if (documento.getVeiculo() != null) {
            Veiculo veiculoExistente =
                    veiculoService.buscarId(
                            documento.getVeiculo().getIdVeiculo()
                    );
            documento.setVeiculo(veiculoExistente);
            return documentoRepository.save(documento);

        } else {
            throw new DadosInvalidosException("Veiculo é obrigatorio");
        }
    }

    public List<Documento> mostrarTodos() {
        return documentoRepository.findAll();
    }

    public Documento buscarId(Long id) {
        return documentoRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Documento não encontrado"));
    }

    public void deletarId(Long id) {
        if (documentoRepository.existsById(id)) {
            documentoRepository.deleteById(id);
        } else {
            throw new RecursoNaoEncontradoException("Documento nao encontrado");
        }
    }

    public Documento atualizar(Long id, Documento novosDados) {

        Documento documento = documentoRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Documento não encontrado")
                );

        if (novosDados.getVeiculo() == null) {
            throw new DadosInvalidosException("Veiculo é obrigatorio");
        }

        Veiculo veiculoExiste =
                veiculoService.buscarId(
                        novosDados.getVeiculo().getIdVeiculo()
                );

        documento.setVeiculo(veiculoExiste);
        documento.setCaminhoArquivo(novosDados.getCaminhoArquivo());
        documento.setNome(novosDados.getNome());
        documento.setTipoDocumento(novosDados.getTipoDocumento());
        return documentoRepository.save(documento);
    }


}
