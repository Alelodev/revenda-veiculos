package br.com.revenda.service;

import br.com.revenda.model.Usuario;
import br.com.revenda.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;

    }

    public Usuario salvar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> mostrarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarId(Long id) {
        return usuarioRepository.findById(id);
    }

    public void deletarId(Long id) {
        usuarioRepository.deleteById(id);
    }

    public Usuario atualizar(Long id, Usuario novosDados) {
        Optional<Usuario> usuarioExiste = usuarioRepository.findById(id);
        if (usuarioExiste.isPresent()) {
            Usuario usuario = usuarioExiste.get();
            usuario.setLogin(novosDados.getLogin());
            usuario.setSenha(novosDados.getSenha());
            return usuarioRepository.save(usuario);
        }
        return null;
    }

}

