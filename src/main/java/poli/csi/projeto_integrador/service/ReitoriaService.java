package poli.csi.projeto_integrador.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poli.csi.projeto_integrador.dto.request.ReitoriaReqDto;
import poli.csi.projeto_integrador.dto.request.VerbaReqDto;
import poli.csi.projeto_integrador.dto.response.VerbaResDto;
import poli.csi.projeto_integrador.model.Reitoria;
import poli.csi.projeto_integrador.model.RepasseReitoria;
import poli.csi.projeto_integrador.model.Usuario;
import poli.csi.projeto_integrador.repository.ReitoriaRepository;
import poli.csi.projeto_integrador.repository.RepasseReitoriaRepository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

@Service
@AllArgsConstructor
public class ReitoriaService {
    private final ReitoriaRepository reitoriaRepository;
    private final RepasseReitoriaRepository repasseReitoriaRepository;

    public Reitoria buscarReitoria(Long id) {
        return reitoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reitoria não encontrada!"));
    }

    @Transactional
    public boolean alterarStatusReitoria(Long id) {
        Reitoria r = reitoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reitoria não encontrada!"));

        r.setStatus(!r.getStatus());

        reitoriaRepository.save(r);
        return true;
    }

    @Transactional
    public boolean alterarReitoria(ReitoriaReqDto dto) {
        Reitoria r = reitoriaRepository.findById(dto.id())
                .orElseThrow(() -> new EntityNotFoundException("Reitoria não encontrada!"));

        r.setNome(dto.name());
        r.setEmail(dto.email());
        r.setTelefone(dto.telefone());
        r.setResponsavel(dto.responsavel());

        if(!dto.login().isEmpty()) {
            r.setLogin(dto.login().trim());
        }

        if(!dto.senha().isEmpty()) {
            BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
            String encrypted = bcrypt.encode(dto.senha().trim());
            r.setSenha(encrypted);
        }

        reitoriaRepository.save(r);
        return true;
    }

    @Transactional
    public boolean salvarReitoria(ReitoriaReqDto dto) {
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        String encrypted = bcrypt.encode(dto.senha().trim());

        Reitoria r = new Reitoria();
        r.setNome(dto.name());
        r.setEmail(dto.email());
        r.setTelefone(dto.telefone());
        r.setStatus(true);
        r.setRole(Usuario.TipoUsuario.REITORIA);
        r.setVerba(BigDecimal.ZERO);
        r.setGasto(BigDecimal.ZERO);
        r.setCnpj(dto.cnpj());
        r.setResponsavel(dto.responsavel());
        r.setLogin(dto.login().trim());
        r.setSenha(encrypted);

        reitoriaRepository.save(r);
        return true;
    }

    public List<Reitoria> buscarReitorias() {
        return reitoriaRepository.findAll();
    }

    @Transactional
    public boolean adicionarVerba(VerbaReqDto dto, String timezone) {
        Reitoria reitoria = reitoriaRepository.findById(dto.reitoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Reitoria não encontrada!"));

        Timestamp dataTempo = gerarTimestamp(timezone);

        RepasseReitoria repasse = new RepasseReitoria();
        repasse.setReitoria(reitoria);
        repasse.setValor(dto.verbaReitoria());
        repasse.setDataTempo(dataTempo);
        repasseReitoriaRepository.save(repasse);

        BigDecimal soma = reitoria.getVerba().add(dto.verbaReitoria());
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
