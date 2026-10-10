package br.com.revenda.service;

import br.com.revenda.exception.RecursoNaoEncontradoException;
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

    public Usuario buscarId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Usuário não encontrado")
                );
    }

    public void deletarId(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
        } else {
            throw new RecursoNaoEncontradoException("Usuário não encontrado");
        }
    }

    public Usuario atualizar(Long id, Usuario novosDados) {
        Optional<Usuario> usuarioExiste = usuarioRepository.findById(id);
        if (usuarioExiste.isPresent()) {
            Usuario usuario = usuarioExiste.get();
            usuario.setLogin(novosDados.getLogin());
            usuario.setSenha(novosDados.getSenha());
            return usuarioRepository.save(usuario);
        }
         throw new RecursoNaoEncontradoException("Usuário não encontrado");
    }

}

