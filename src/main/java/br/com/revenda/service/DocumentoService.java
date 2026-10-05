package br.com.revenda.service;

import br.com.revenda.model.Documento;
import br.com.revenda.repository.DocumentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentoService {

    private DocumentoRepository documentoRepository;

    public DocumentoService(DocumentoRepository documentoRepository) {
        this.documentoRepository = documentoRepository;
    }

    public Documento salvar(Documento documento) {
        return documentoRepository.save(documento);
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
}
