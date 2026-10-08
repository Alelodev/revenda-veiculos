package br.com.revenda.controller;

import br.com.revenda.model.Documento;
import br.com.revenda.service.DocumentoService;
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
    public List<Documento> listarDocumentos() {
        return documentoService.mostrarTodos();
    }

    @GetMapping("/documentos/{id}")
    @ResponseBody
    public Optional<Documento> buscarPorId(@PathVariable Long id) {
        return documentoService.buscarId(id);
    }

    @PostMapping("/documentos")
    @ResponseBody
    public Documento cadastrar(@RequestBody Documento documento) {
        return documentoService.salvar(documento);
    }

    @PutMapping("/documentos/{id}")
    @ResponseBody
    public Documento atualizar(@PathVariable Long id, @RequestBody Documento documento) {
        return documentoService.atualizar(id, documento);
    }

    @DeleteMapping("/documentos/{id}")
    @ResponseBody
    public void deletar(@PathVariable Long id) {
        documentoService.deletarId(id);
    }
}
