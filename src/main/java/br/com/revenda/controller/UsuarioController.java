package br.com.revenda.controller;

import br.com.revenda.model.Usuario;
import br.com.revenda.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UsuarioController {

    UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/usuarios")
    @ResponseBody
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> mostrarUsuarios = usuarioService.mostrarTodos();
        return ResponseEntity.ok(mostrarUsuarios);
    }

    @GetMapping("/usuarios/{id}")
    @ResponseBody
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarId(id);
        return ResponseEntity.ok(usuario);
    }

    @PostMapping("/usuarios")
    @ResponseBody
    public ResponseEntity<Usuario> cadastrar(@Valid @RequestBody Usuario usuario) {
        Usuario usuarioSalvo = usuarioService.salvar(usuario);

        return ResponseEntity.status(201).body(usuarioSalvo);
    }

    @PutMapping("/usuarios/{id}")
    @ResponseBody
    public ResponseEntity<Usuario> atualizar(@PathVariable Long id, @Valid @RequestBody Usuario usuario) {
        Usuario usuarioAtualizado = usuarioService.atualizar(id, usuario);
        return ResponseEntity.ok(usuarioAtualizado);
    }

    @DeleteMapping("/usuarios/{id}")
    @ResponseBody
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletarId(id);
        return ResponseEntity.noContent().build();
    }
}

