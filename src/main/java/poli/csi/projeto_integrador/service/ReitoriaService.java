package poli.csi.projeto_integrador.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poli.csi.projeto_integrador.dto.request.AlterarReitoriaDto;
import poli.csi.projeto_integrador.dto.request.ReitoriaVerbaDto;
import poli.csi.projeto_integrador.dto.request.SalvarReitoriaDto;
import poli.csi.projeto_integrador.dto.response.StatusResDto;
import poli.csi.projeto_integrador.dto.response.VerbaResDto;
import poli.csi.projeto_integrador.exception.CustomException;
import poli.csi.projeto_integrador.model.Reitoria;
import poli.csi.projeto_integrador.model.RepasseReitoria;
import poli.csi.projeto_integrador.model.Usuario;
import poli.csi.projeto_integrador.repository.ReitoriaRepository;
import poli.csi.projeto_integrador.repository.RepasseReitoriaRepository;
import poli.csi.projeto_integrador.repository.UsuarioRepository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReitoriaService {
    private final ReitoriaRepository reitoriaRepository;
    private final RepasseReitoriaRepository repasseReitoriaRepository;
    private final UsuarioRepository usuarioRepository;

    public Reitoria buscarReitoria(Long id) {
        return reitoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reitoria não encontrada!"));
    }

    @Transactional
    public StatusResDto alterarStatusReitoria(Long id) {
        Reitoria r = reitoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reitoria não encontrada!"));

        r.setStatus(!r.getStatus());

        reitoriaRepository.save(r);
        StatusResDto res = new StatusResDto(r.getStatus(), "Status alterado com sucesso!");
        return res;
    }

    @Transactional
    public boolean alterarReitoria(AlterarReitoriaDto dto) {
        Optional<Usuario> usuario = usuarioRepository.findUsuarioByEmail(dto.email());

        if(usuario.isPresent() && !usuario.get().getId().equals(dto.id())) {
            throw new CustomException("E-mail informado já tem registro no sistema!");
        }

        Reitoria r = reitoriaRepository.findById(dto.id())
                .orElseThrow(() -> new EntityNotFoundException("Reitoria não encontrada!"));

        r.setNome(dto.nome().trim());
        r.setEmail(dto.email().trim());
        r.setTelefone(dto.telefone());
        r.setResponsavel(dto.responsavel().trim());

        reitoriaRepository.save(r);
        return true;
    }

    @Transactional
    public boolean salvarReitoria(SalvarReitoriaDto dto) {
        Optional<Usuario> usuario = usuarioRepository.findUsuarioByLogin(dto.login());

        if(usuario.isPresent()) {
            throw new CustomException("Nome de usuário informado já tem registro no sistema!");
        }

        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        String encrypted = bcrypt.encode(dto.senha().trim());

        Reitoria r = new Reitoria();
        r.setNome(dto.nome().trim());
        r.setEmail(dto.email().trim());
        r.setTelefone(dto.telefone().trim());
        r.setStatus(true);
        r.setRole(Usuario.TipoUsuario.REITORIA);
        r.setVerba(BigDecimal.ZERO);
        r.setGasto(BigDecimal.ZERO);
        r.setCnpj(dto.cnpj().trim());
        r.setResponsavel(dto.responsavel().trim());
        r.setLogin(dto.login().trim());
        r.setSenha(encrypted);

        reitoriaRepository.save(r);
        return true;
    }

    public List<Reitoria> buscarReitorias() {
        return reitoriaRepository.findAll();
    }

    @Transactional
    public boolean adicionarVerba(ReitoriaVerbaDto dto, String timezone) {
        Reitoria reitoria = reitoriaRepository.findById(dto.id())
                .orElseThrow(() -> new EntityNotFoundException("Reitoria não encontrada!"));

        Timestamp dataTempo = gerarTimestamp(timezone);

        RepasseReitoria repasse = new RepasseReitoria();
        repasse.setReitoria(reitoria);
        repasse.setValor(dto.verba());
        repasse.setDataTempo(dataTempo);
        repasseReitoriaRepository.save(repasse);

        BigDecimal soma = reitoria.getVerba().add(dto.verba());
        reitoria.setVerba(soma);
        reitoriaRepository.save(reitoria);

        return true;
    }

    private Timestamp gerarTimestamp(String timezone) {
       Instant i = Instant.now().atZone(ZoneId.of(timezone)).toInstant();
       return Timestamp.from(i);
    }

    public VerbaResDto buscarVerbaReitoria(Long id) {
        Reitoria reitoria = reitoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reitoria não encontrada!"));

        return new VerbaResDto(reitoria.getVerba(), reitoria.getGasto());
    }
}
