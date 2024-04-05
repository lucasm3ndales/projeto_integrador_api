package poli.csi.projeto_integrador.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import poli.csi.projeto_integrador.dto.request.AlterarLoginDto;
import poli.csi.projeto_integrador.exception.CustomException;
import poli.csi.projeto_integrador.model.Usuario;
import poli.csi.projeto_integrador.repository.UsuarioRepository;

import java.util.Optional;

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

    public boolean alterarLogin(AlterarLoginDto dto) {
        Optional<Usuario> usuario = usuarioRepository.findUsuarioByEmail(dto.email());

        if(usuario.isEmpty()) {
            throw new CustomException("E-mail informado não pertence a nenhum usuário do sistema!");
        }

        Usuario u = usuario.get();

        if(dto.login() != null) {
            u.setLogin(dto.login().trim());
        }

        if(dto.senha() != null) {
            BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
            String encrypted = bCrypt.encode(dto.senha().trim());
            u.setSenha(encrypted);
        }

        usuarioRepository.save(u);
        return true;
    }
}
