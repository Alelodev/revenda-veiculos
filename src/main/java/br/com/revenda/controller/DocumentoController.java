package br.com.revenda.controller;

import br.com.revenda.model.Documento;
import br.com.revenda.service.DocumentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
        Optional<Documento> documento = documentoService.buscarId(id);
        if (documento.isPresent()) {
            return ResponseEntity.ok(documento.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/documentos")
    @ResponseBody
    public ResponseEntity<Documento> cadastrar(@RequestBody Documento documento) {
        Documento documentoSalvo = documentoService.salvar(documento);

       if(documentoSalvo != null){
           return ResponseEntity.status(201).body(documentoSalvo);
       } else {
           return ResponseEntity.badRequest().build();
       }
    }

    @PutMapping("/documentos/{id}")
    @ResponseBody
    public ResponseEntity<Documento> atualizar(@PathVariable Long id, @RequestBody Documento documento) {
        Documento documentoAtualizado = documentoService.atualizar(id, documento);
        if (documentoAtualizado != null) {
            return ResponseEntity.ok(documentoAtualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/documentos/{id}")
    @ResponseBody
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        Optional<Documento> documento = documentoService.buscarId(id);
        if (documento.isPresent()) {
            documentoService.deletarId(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
