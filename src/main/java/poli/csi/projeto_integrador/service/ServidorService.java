package poli.csi.projeto_integrador.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import poli.csi.projeto_integrador.dto.filter.FiltroServidor;
import poli.csi.projeto_integrador.dto.request.AlterarServidorDto;
import poli.csi.projeto_integrador.dto.request.SalvarServidorDto;
import poli.csi.projeto_integrador.dto.response.StatusResDto;
import poli.csi.projeto_integrador.exception.CustomException;
import poli.csi.projeto_integrador.model.Servidor;
import poli.csi.projeto_integrador.model.Usuario;
import poli.csi.projeto_integrador.repository.ServidorRepository;
import poli.csi.projeto_integrador.repository.UsuarioRepository;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ServidorService {
    private final ServidorRepository servidorRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public boolean salvarServidor(SalvarServidorDto dto) {
        Optional<Usuario> usuario = usuarioRepository.findUsuarioByLogin(dto.login());

        if(usuario.isPresent()) {
            throw new CustomException("Nome de usuário informado já tem registro no sistema!");
        }

        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        String encrypted = bcrypt.encode(dto.senha().trim());

        Servidor s = new Servidor();
        s.setNome(dto.nome().trim());
        s.setEmail(dto.email().trim());
        s.setTelefone(dto.telefone().trim());
        s.setStatus(true);
        s.setRole(Usuario.TipoUsuario.REITORIA);
        s.setMatricula(dto.matricula().trim());
        s.setLogin(dto.login().trim());
        s.setSenha(encrypted);

        servidorRepository.save(s);
        return true;
    }

    @Transactional
    public boolean alterarServidor(AlterarServidorDto dto) {
        Optional<Usuario> usuario = usuarioRepository.findUsuarioByEmail(dto.email());

        if(usuario.isPresent() && !usuario.get().getId().equals(dto.id())) {
            throw new CustomException("E-mail informado já tem registro no sistema!");
        }

        Servidor s = servidorRepository.findById(dto.id())
                .orElseThrow(() -> new EntityNotFoundException("Servidor não encontrado!"));
        
        s.setNome(dto.nome().trim());
        s.setEmail(dto.email().trim());
        s.setTelefone(dto.telefone().trim());
        s.setStatus(true);
        s.setRole(Usuario.TipoUsuario.REITORIA);

        servidorRepository.save(s);
        return true;
    }

    @Transactional
    public StatusResDto alterarStatusServidor(Long id) {
        Servidor s = servidorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Servidor não encontrado!"));
        
        s.setStatus(!s.getStatus());
        
        servidorRepository.save(s);
        return new StatusResDto(s.getStatus(), "Status alterado com sucesso!");
    }

    public Servidor buscarServidor(Long id) {
        return servidorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Servidor não encontrado!"));
    }

    public Page<Servidor> buscarServidores(Pageable pageable, FiltroServidor filtro) {
        if(isFiltro(filtro)) {
            Specification<Servidor> spec = ServidorRepository.servidorSpec(filtro);
            return servidorRepository.findAll(spec, pageable);
        }
        return servidorRepository.findAll(pageable);
    }

    private boolean isFiltro(FiltroServidor filtro) {
        return filtro.nome() != null && !filtro.nome().isBlank() ||
                filtro.matricula() != null && !filtro.matricula().isBlank();
    }
}
