package br.com.revenda.controller;

import br.com.revenda.model.Usuario;
import br.com.revenda.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class UsuarioController {

    UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/usuarios")
    @ResponseBody
    public ResponseEntity<List<Usuario>> listarUsuarios(){
        List<Usuario> mostrarUsuarios = usuarioService.mostrarTodos();
        return ResponseEntity.ok(mostrarUsuarios);
    }

    @GetMapping("/usuarios/{id}")
    @ResponseBody
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id){
        Optional<Usuario> usuario = usuarioService.buscarId(id);
        if (usuario.isPresent()) {
            return  ResponseEntity.ok(usuario.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/usuarios")
    @ResponseBody
    public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario){
        Usuario usuarioSalvo = usuarioService.salvar(usuario);

        return ResponseEntity.status(201).body(usuarioSalvo);
    }

    @PutMapping("/usuarios/{id}")
    @ResponseBody
    public ResponseEntity<Usuario> atualizar(@PathVariable Long id, @RequestBody Usuario usuario){
        Usuario usuarioAtualizado = usuarioService.atualizar(id, usuario);
        if (usuarioAtualizado != null) {
            return ResponseEntity.ok(usuarioAtualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/usuarios/{id}")
    @ResponseBody
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        Optional<Usuario> usuario = usuarioService.buscarId(id);

        if(usuario.isPresent()){
            usuarioService.deletarId(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
