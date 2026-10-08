package br.com.revenda.controller;

import br.com.revenda.model.Proprietario;
import br.com.revenda.model.Veiculo;
import br.com.revenda.service.VeiculoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class VeiculoController {

    private VeiculoService veiculoService;

    public VeiculoController(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }

    @GetMapping("/veiculos")
    @ResponseBody
    public List<Veiculo> listarVeiculos(){
        return veiculoService.mostrarTodos();
    }

    @GetMapping("/veiculos/{id}")
    @ResponseBody
    public Optional<Veiculo> buscarPorId(@PathVariable Long id){
        return veiculoService.buscarId(id);
    }

    @PostMapping("/veiculos")
    @ResponseBody
    public Veiculo cadastrar(@RequestBody Veiculo veiculo){
        return veiculoService.salvar(veiculo);
    }

    @PutMapping("/veiculos/{id}")
    @ResponseBody
    public Veiculo atualizar(@PathVariable Long id, @RequestBody Veiculo veiculo){
        return veiculoService.atualizar(id, veiculo);
    }

    @DeleteMapping("/veiculos/{id}")
    @ResponseBody
    public void deletar(@PathVariable Long id){
        veiculoService.deletarId(id);
    }

}
