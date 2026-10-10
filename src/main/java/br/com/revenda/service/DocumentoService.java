package br.com.revenda.service;

import br.com.revenda.exception.DadosInvalidosException;
import br.com.revenda.exception.RecursoNaoEncontradoException;
import br.com.revenda.model.Documento;
import br.com.revenda.model.Veiculo;
import br.com.revenda.repository.DocumentoRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

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

    public Documento salvarArquivo(MultipartFile arquivo, Long idVeiculo, String tipoDocumento) throws IOException {
        if (arquivo.isEmpty()) {
            throw new DadosInvalidosException("Arquivo é obrigatório");
        }
        Veiculo veiculo = veiculoService.buscarId(idVeiculo);
        Path pasta = Path.of(
                "uploads",
                "veiculos",
                idVeiculo.toString()
        );
        Files.createDirectories(pasta);
        String nomeOriginal = arquivo.getOriginalFilename();

        String nomeArquivo = UUID.randomUUID() + "_" + nomeOriginal;

        Path destino = pasta.resolve(nomeArquivo);
        Files.copy(
                arquivo.getInputStream(),
                destino
        );
        Documento documento = new Documento();

        documento.setVeiculo(veiculo);
        documento.setNome(nomeOriginal);
        documento.setTipoDocumento(tipoDocumento);
        documento.setCaminhoArquivo(destino.toString());

        return documentoRepository.save(documento);
    }

    public Resource baixarArquivo(Long idDocumento) throws MalformedURLException {

        Documento documento = buscarId(idDocumento);

        Path caminho = Path.of(documento.getCaminhoArquivo());

        Resource recurso = new UrlResource(caminho.toUri());

        if (!recurso.exists()) {
            throw new RecursoNaoEncontradoException("Arquivo não encontrado");
        }

        return recurso;
    }
}
