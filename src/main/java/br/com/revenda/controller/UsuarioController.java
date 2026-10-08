package br.com.revenda.controller;

import br.com.revenda.model.Usuario;
import br.com.revenda.service.UsuarioService;
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
    public List<Usuario> listarUsuarios(){
        return usuarioService.mostrarTodos();
    }

    @GetMapping("/usuarios/{id}")
    @ResponseBody
    public Optional<Usuario> buscarPorId(@PathVariable Long id){
        return usuarioService.buscarId(id);
    }

    @PostMapping("/usuarios")
    @ResponseBody
    public Usuario cadastrar(@RequestBody Usuario usuario){
        return usuarioService.salvar(usuario);
    }

    @PutMapping("/usuarios/{id}")
    @ResponseBody
    public Usuario atualizar(@PathVariable Long id, @RequestBody Usuario usuario){
        return usuarioService.atualizar(id, usuario);
    }

    @DeleteMapping("/usuarios/{id}")
    @ResponseBody
    public void deletar(@PathVariable Long id){
        usuarioService.deletarId(id);
    }
}
