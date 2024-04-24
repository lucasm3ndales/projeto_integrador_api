package poli.csi.projeto_integrador.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import poli.csi.projeto_integrador.dto.filtro.UsuarioFiltro;
import poli.csi.projeto_integrador.dto.request.UsuarioAlterarDto;
import poli.csi.projeto_integrador.dto.request.UsuarioSalvarDto;
import poli.csi.projeto_integrador.exception.CustomException;
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

    @Transactional
    public boolean salvar(UsuarioSalvarDto dto) {
        Usuario isExist = usuarioRepository.findUsuarioByEmailOrLogin(dto.email().trim(), dto.login().trim())
                .orElse(null);

        if(isExist != null) {
            throw new CustomException("E-mail ou Nome de usuário já em uso no sistema!");
        }

        Usuario usuario = new Usuario();

        usuario.setNome(dto.nome().toLowerCase().trim());
        usuario.setEmail(dto.email().trim());
        usuario.setTelefone(dto.telefone().trim());
        usuario.setLogin(dto.login().trim());
        usuario.setSenha(dto.senha().trim());
        usuario.setMatricula(dto.matricula().trim());
        usuario.setAtivo(true);
        usuario.setRole(Usuario.TipoUsuario.SERVIDOR);

        usuarioRepository.save(usuario);
        return true;
    }

    @Transactional
    public boolean alterar(UsuarioAlterarDto dto) {
        Usuario usuario = usuarioRepository.findById(dto.id())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado!"));

        Usuario isExist = usuarioRepository.findUsuarioByEmailOrLogin(dto.email().trim(), dto.login().trim())
                .orElse(null);

        if(isExist != null && !isExist.equals(usuario)) {
            throw new CustomException("E-mail ou Nome de usuário já em uso no sistema!");
        }

        usuario.setNome(dto.nome().toLowerCase().trim());
        usuario.setEmail(dto.email().trim());
        usuario.setTelefone(dto.telefone().trim());

        if(dto.login() != null) {
            usuario.setLogin(dto.login().trim());
        }

        if(dto.senha() != null) {
            usuario.setSenha(dto.senha().trim());
        }

        usuarioRepository.save(usuario);
        return true;
    }

    @Transactional
    public boolean alterarStatus(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado!"));


        usuario.setAtivo(!usuario.getAtivo());

        usuarioRepository.save(usuario);
        return true;
    }

    public Usuario buscarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado!"));
    }

    public Page<Usuario> buscarUsuarios(Pageable pageable, UsuarioFiltro filtros) {
        if(isFiltro(filtros)) {
            Specification<Usuario> spec = UsuarioRepository.specUsuario(filtros);
            return usuarioRepository.findAll(spec, pageable);
        }
        return usuarioRepository.findAll(pageable);
    }

    private boolean isFiltro(UsuarioFiltro f) {
        return f.search() != null ||
                f.role() != null ||
                f.ativo() != null;
    }
}
