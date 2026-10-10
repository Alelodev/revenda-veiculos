package br.com.revenda.controller;

import br.com.revenda.model.Documento;
import br.com.revenda.service.DocumentoService;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Controller
public class DocumentoController {

    private DocumentoService documentoService;

    public DocumentoController(DocumentoService documentoService) {
        this.documentoService = documentoService;
    }

    @GetMapping("/documentos")
    @ResponseBody
    public ResponseEntity<List<Documento>> listarDocumentos() {
        List<Documento> mostrarDocumentos = documentoService.mostrarTodos();
        return ResponseEntity.ok(mostrarDocumentos);
    }

    @GetMapping("/documentos/{id}")
    @ResponseBody
    public ResponseEntity<Documento> buscarPorId(@PathVariable Long id) {
        Documento documento = documentoService.buscarId(id);
        return ResponseEntity.ok(documento);
    }

    @PostMapping("/documentos")
    @ResponseBody
    public ResponseEntity<Documento> cadastrar(@Valid @RequestBody Documento documento) {
        Documento documentoSalvo = documentoService.salvar(documento);

        return ResponseEntity.status(201).body(documentoSalvo);

    }

    @PutMapping("/documentos/{id}")
    @ResponseBody
    public ResponseEntity<Documento> atualizar(@PathVariable Long id, @Valid @RequestBody Documento documento) {
        Documento documentoAtualizado = documentoService.atualizar(id, documento);
        return ResponseEntity.ok(documentoAtualizado);

    }

    @DeleteMapping("/documentos/{id}")
    @ResponseBody
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        documentoService.deletarId(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/documentos/upload")
    @ResponseBody
    public ResponseEntity<Documento> upload(
            @RequestParam("arquivo") MultipartFile arquivo,
            @RequestParam("idVeiculo") Long idVeiculo,
            @RequestParam("tipoDocumento") String tipoDocumento
    ) throws IOException {
        Documento documentoSalvo =
                documentoService.salvarArquivo(
                        arquivo,
                        idVeiculo,
                        tipoDocumento
                );
        return ResponseEntity.status(201).body(documentoSalvo);
    }

    @GetMapping("/documentos/{id}/download")
    @ResponseBody
    public ResponseEntity<Resource> download(@PathVariable Long id) throws IOException {

        Resource recurso = documentoService.baixarArquivo(id);

        Documento documento = documentoService.buscarId(id);

        String tipoConteudo = Files.probeContentType(
                Path.of(documento.getCaminhoArquivo())
        );


        if (tipoConteudo == null) {
            tipoConteudo = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + documento.getNome() + "\""

                )
                .contentType(MediaType.parseMediaType(tipoConteudo))
                .body(recurso);
    }
}
