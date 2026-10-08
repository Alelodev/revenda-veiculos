package br.com.revenda.service;

import br.com.revenda.model.Documento;
import br.com.revenda.model.Veiculo;
import br.com.revenda.repository.DocumentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentoService {

    private DocumentoRepository documentoRepository;
    private VeiculoService veiculoService;

    public DocumentoService(DocumentoRepository documentoRepository, VeiculoService veiculoService) {
        this.documentoRepository = documentoRepository;
        this.veiculoService = veiculoService;
    }

    public Documento salvar(Documento documento) {
        if (documento.getVeiculo() != null){
            Optional<Veiculo> veiculoExistente =
                    veiculoService.buscarId(
                            documento.getVeiculo().getIdVeiculo()
                    );
            if(veiculoExistente.isPresent()){
                documento.setVeiculo(veiculoExistente.get());
                return documentoRepository.save(documento);
            }
        }
        return null;
    }

    public List<Documento> mostrarTodos() {
        return documentoRepository.findAll();
    }

    public Optional<Documento> buscarId(Long id) {
        return documentoRepository.findById(id);
    }

    public void deletarId(Long id) {
        documentoRepository.deleteById(id);
    }

    public Documento atualizar(Long id, Documento novosDados){
        Optional<Documento> documentoExistente = documentoRepository.findById(id);
        if(documentoExistente.isPresent()){
            Documento documento = documentoExistente.get();
            if (novosDados.getVeiculo() != null){
                Optional<Veiculo> veiculoExistente = veiculoService.buscarId(
                    novosDados.getVeiculo().getIdVeiculo()
                );
                if(veiculoExistente.isPresent()){
                    documento.setVeiculo(veiculoExistente.get());
                    documento.setCaminhoArquivo(novosDados.getCaminhoArquivo());
                    documento.setNome(novosDados.getNome());
                    documento.setTipoDocumento(novosDados.getTipoDocumento());
                    return documentoRepository.save(documento);
                }

            }
        }
        return null;
    }
}
