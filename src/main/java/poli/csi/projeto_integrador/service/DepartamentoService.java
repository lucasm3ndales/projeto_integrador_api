package poli.csi.projeto_integrador.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poli.csi.projeto_integrador.dto.filter.FiltroDepartamento;
import poli.csi.projeto_integrador.dto.request.AlterarDepartamentoDto;
import poli.csi.projeto_integrador.dto.request.DepartamentoVerbaDto;
import poli.csi.projeto_integrador.dto.request.SalvarDepartamentoDto;
import poli.csi.projeto_integrador.dto.response.StatusResDto;
import poli.csi.projeto_integrador.dto.response.VerbaResDto;
import poli.csi.projeto_integrador.exception.CustomException;
import poli.csi.projeto_integrador.model.Departamento;
import poli.csi.projeto_integrador.model.Reitoria;
import poli.csi.projeto_integrador.model.RepasseDepartamento;
import poli.csi.projeto_integrador.model.Usuario;
import poli.csi.projeto_integrador.repository.DepartamentoRepository;
import poli.csi.projeto_integrador.repository.ReitoriaRepository;
import poli.csi.projeto_integrador.repository.RepasseDepartamentoRepository;
import poli.csi.projeto_integrador.repository.UsuarioRepository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DepartamentoService {
    private final UsuarioRepository usuarioRepository;
    private final DepartamentoRepository departamentoRepository;
    private final RepasseDepartamentoRepository repasseDepartamentoRepository;
    private final ReitoriaRepository reitoriaRepository;

    @Transactional
    public boolean salvarDepartamento(SalvarDepartamentoDto dto) {
        Optional<Usuario> usuario = usuarioRepository.findUsuarioByLogin(dto.login());

        if(usuario.isPresent()) {
            throw new CustomException("Nome de usuário informado já tem registro no sistema!");
        }

        Departamento dep = new Departamento();
        BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();
        String encrypted = bCrypt.encode(dto.senha().trim());
        dep.setNome(dto.nome().trim());
        dep.setEmail(dto.email().trim());
        dep.setTelefone(dto.telefone().trim());
        dep.setResponsavel(dto.responsavel().trim());
        dep.setLogin(dto.login().trim());
        dep.setSenha(encrypted);
        dep.setStatus(true);
        dep.setRole(Usuario.TipoUsuario.DEPARTAMENTO);
        dep.setVerba(BigDecimal.ZERO);
        dep.setGasto(BigDecimal.ZERO);
        
        departamentoRepository.save(dep);
        return true;
    }
    
    @Transactional
    public boolean alterarDepartamento(AlterarDepartamentoDto dto) {
        Optional<Usuario> usuario = usuarioRepository.findUsuarioByLogin(dto.login());

        if(usuario.isPresent()) {
            throw new CustomException("Nome de usuário informado já tem registro no sistema!");
        }

        Departamento dep = departamentoRepository.findById(dto.id())
                .orElseThrow(() -> new EntityNotFoundException("Departamento não encontrado!"));
     
        dep.setNome(dto.nome().trim());
        dep.setEmail(dto.email().trim());
        dep.setTelefone(dto.telefone().trim());
        dep.setResponsavel(dto.responsavel().trim());
        
        if(!dto.login().isEmpty()) {
            dep.setLogin(dto.login().trim());
        }

        if(!dto.senha().isEmpty()) {
            BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
            String encrypted = bcrypt.encode(dto.senha().trim());
            dep.setSenha(encrypted);
        }
        departamentoRepository.save(dep);
        return true;
    }

    public Departamento buscarDepartamento(Long id) {
        return departamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Departamento não encontrado!"));
    }

    public Page<Departamento> buscarDepartamentos(Pageable pageable, FiltroDepartamento filtro) {
        if(isFilter(filtro)) {
            Specification<Departamento> spec = DepartamentoRepository.departamentoSpec(filtro);
            return departamentoRepository.findAll(spec, pageable);
        }
        return departamentoRepository.findAll(pageable);
    }

    private boolean isFilter(FiltroDepartamento filtro) {
        return filtro.nome() != null && !filtro.nome().isBlank()||
                filtro.status() != null ||
                filtro.responsavel() != null && !filtro.responsavel().isBlank();
    }

    @Transactional
    public boolean adicionarVerba(DepartamentoVerbaDto dto, String timezone) {
        Departamento dep = departamentoRepository.findById(dto.departamentoId())
                .orElseThrow(() -> new EntityNotFoundException("Departamento não encontrado!"));

        Reitoria reitoria = reitoriaRepository.findById(dto.reitoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Reitoria não encontrada!"));

        RepasseDepartamento  rd = new RepasseDepartamento();
        rd.setReitoria(reitoria);
        rd.setDepartamento(dep);
        rd.setValor(dto.verbaDepartamento());
        rd.setDataTempo(gerarTimestamp(timezone));
        repasseDepartamentoRepository.save(rd);

        BigDecimal somaDep = dep.getVerba().add(dto.verbaDepartamento());
        BigDecimal subReit = reitoria.getVerba().subtract(dto.verbaDepartamento());
        dep.setVerba(somaDep);
        reitoria.setVerba(subReit);

        departamentoRepository.save(dep);
        reitoriaRepository.save(reitoria);

        return true;
    }

    private Timestamp gerarTimestamp(String timezone) {
        Instant i = Instant.now().atZone(ZoneId.of(timezone)).toInstant();
        return Timestamp.from(i);
    }

    public VerbaResDto buscarVerbaDepartamento(Long id) {
        Departamento dep = departamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Departamento não encontrado!"));

        return new VerbaResDto(
                dep.getVerba(), dep.getGasto()
        );
    }

    @Transactional
    public StatusResDto alterarStatusDepartamento(Long id) {
        Departamento dep = departamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Departamento não encontrado!"));
        dep.setStatus(!dep.getStatus());
        departamentoRepository.save(dep);
        return new StatusResDto(dep.getStatus(), "Status alterado com sucesso!");
    }
}
