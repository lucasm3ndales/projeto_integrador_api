package poli.csi.projeto_integrador.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import poli.csi.projeto_integrador.model.Usuario;
import poli.csi.projeto_integrador.repository.UsuarioRepository;

@Service
@AllArgsConstructor
public class UsuarioService implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findUsuarioByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Nome de usuário ou senha incorretos!"));
        if(usuario != null) {
            UserDetails userDetails = User.withUsername(usuario.getLogin())
                    .password(usuario.getSenha())
                    .authorities(usuario.getRole().name()).build();
            return userDetails;
        }
        return null;
    }

    public Usuario getUsuarioByUsername(String login) throws UsernameNotFoundException {
        return usuarioRepository.findUsuarioByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado no sistema!"));
    }
}
