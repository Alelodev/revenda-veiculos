package br.com.revenda.controller;

import br.com.revenda.model.Veiculo;
import br.com.revenda.service.VeiculoService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class VeiculoController {

    private VeiculoService veiculoService;

    public VeiculoController(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }

    @GetMapping("/veiculos")
    @ResponseBody
    public ResponseEntity<List<Veiculo>> listarVeiculos() {
        List<Veiculo> mostrarVeiculos = veiculoService.mostrarTodos();
        return ResponseEntity.ok(mostrarVeiculos);
    }

    @GetMapping("/veiculos/{id}")
    @ResponseBody
    public ResponseEntity<Veiculo> buscarPorId(@PathVariable Long id) {
        Veiculo veiculo = veiculoService.buscarId(id);
        return ResponseEntity.ok(veiculo);
    }

    @PostMapping("/veiculos")
    @ResponseBody
    public ResponseEntity<Veiculo> cadastrar(@RequestBody Veiculo veiculo) {
        Veiculo veiculoSalvo = veiculoService.salvar(veiculo);

        return ResponseEntity.status(201).body(veiculoSalvo);

    }

    @PutMapping("/veiculos/{id}")
    @ResponseBody
    public ResponseEntity<Veiculo> atualizar(@PathVariable Long id, @RequestBody Veiculo veiculo) {
        Veiculo veiculoAtualizado = veiculoService.atualizar(id, veiculo);
        return ResponseEntity.ok(veiculoAtualizado);

    }

    @DeleteMapping("/veiculos/{id}")
    @ResponseBody
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        veiculoService.deletarId(id);
        return ResponseEntity.noContent().build();


    }

}
