package br.com.revenda.security;

import br.com.revenda.model.Usuario;
import br.com.revenda.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ServicoDeUsuarioCustomizado implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public ServicoDeUsuarioCustomizado(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository
                .findByLogin(login)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuário não encontrado")
                );

        return User
                .withUsername(usuario.getLogin())
                .password(usuario.getSenha())
                .roles(usuario.getPerfil())
                .build();
    }
}