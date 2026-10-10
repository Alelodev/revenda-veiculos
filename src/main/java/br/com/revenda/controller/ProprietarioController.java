package br.com.revenda.controller;

import br.com.revenda.model.Proprietario;
import br.com.revenda.model.Usuario;
import br.com.revenda.service.ProprietarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class ProprietarioController {

    private ProprietarioService proprietarioService;

    public ProprietarioController(ProprietarioService proprietarioService){
        this.proprietarioService = proprietarioService;
    }

    @GetMapping("/proprietarios")
    @ResponseBody
    public ResponseEntity<List<Proprietario>> listarProprietarios(){
        List<Proprietario> mostarProprietarios = proprietarioService.mostrarTodos();
        return ResponseEntity.ok(mostarProprietarios);
    }

    @GetMapping("/proprietarios/{id}")
    @ResponseBody
    public ResponseEntity<Proprietario> buscaPorId(@PathVariable Long id){
        Proprietario proprietario = proprietarioService.buscarId(id);
        return ResponseEntity.ok(proprietario);
    }

    @PostMapping("/proprietarios")
    @ResponseBody
    public ResponseEntity<Proprietario> cadastrar(@RequestBody Proprietario proprietario) {
        Proprietario proprietarioSalvo = proprietarioService.salvar(proprietario);

        return ResponseEntity.status(201).body(proprietarioSalvo);
    }


    @PutMapping("/proprietarios/{id}")
    @ResponseBody
    public ResponseEntity<Proprietario> atualizar(@PathVariable Long id, @RequestBody Proprietario proprietario){
        Proprietario proprietarioAtualizado = proprietarioService.atualizar(id, proprietario);
        return ResponseEntity.ok(proprietarioAtualizado);
    }

    @DeleteMapping("/proprietarios/{id}")
    @ResponseBody
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
            proprietarioService.deletarId(id);
            return ResponseEntity.noContent().build();
    }
}
