package poli.csi.projeto_integrador.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poli.csi.projeto_integrador.dto.request.ReitoriaReqDto;
import poli.csi.projeto_integrador.model.Reitoria;
import poli.csi.projeto_integrador.model.Usuario;
import poli.csi.projeto_integrador.repository.ReitoriaRepository;

@Service
@AllArgsConstructor
public class ReitoriaService {
    private final ReitoriaRepository reitoriaRepository;

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
        r.setRole(Usuario.TipoUsuario.REITORIA);
        r.setCnpj(dto.cnpj());
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
}
