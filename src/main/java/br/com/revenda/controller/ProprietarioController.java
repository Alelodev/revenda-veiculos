package br.com.revenda.controller;

import br.com.revenda.model.Proprietario;
import br.com.revenda.service.ProprietarioService;
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
    public List<Proprietario> listarProprietarios(){
        return proprietarioService.mostrarTodos();
    }

    @GetMapping("/proprietarios/{id}")
    @ResponseBody
    public Optional<Proprietario> buscaPorId(@PathVariable Long id){
        return proprietarioService.buscarId(id);
    }

    @PostMapping("/proprietarios")
    @ResponseBody
    public Proprietario cadastrar(@RequestBody Proprietario proprietario) {
        return proprietarioService.salvar(proprietario);
    }

    @DeleteMapping("/proprietarios/{id}")
    @ResponseBody
    public void deletar(@PathVariable Long id) {
        proprietarioService.deletarId(id);
    }

    @PutMapping("/proprietarios/{id}")
    @ResponseBody
    public Proprietario atualizar(@PathVariable Long id, @RequestBody Proprietario proprietario){
        return proprietarioService.atualizar(id, proprietario);

    }
}
