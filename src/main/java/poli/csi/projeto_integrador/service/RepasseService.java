package poli.csi.projeto_integrador.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import poli.csi.projeto_integrador.dto.filter.FiltroRepasseDepartamento;
import poli.csi.projeto_integrador.dto.filter.FiltroRepasseReitoria;
import poli.csi.projeto_integrador.model.RepasseDepartamento;
import poli.csi.projeto_integrador.model.RepasseReitoria;
import poli.csi.projeto_integrador.repository.RepasseDepartamentoRepository;
import poli.csi.projeto_integrador.repository.RepasseReitoriaRepository;

@Service
@AllArgsConstructor
public class RepasseService {
    private final RepasseReitoriaRepository repasseReitoriaRepository;
    private final RepasseDepartamentoRepository repasseDepartamentoRepository;

    public Page<RepasseReitoria> buscarRepassesReitoria(Long id, Pageable pageable, FiltroRepasseReitoria filtro) {
        if(isFiltroReit(filtro)) {
            Specification<RepasseReitoria> spec = RepasseReitoriaRepository.repasseReitoriaSpec(filtro);
            return repasseReitoriaRepository.findAll(spec, pageable);
        }
        return repasseReitoriaRepository.findAllByReitoria_Id(id, pageable);
    }

    private boolean isFiltroReit(FiltroRepasseReitoria filtro) {
        return filtro.dataInicio() != null||
                filtro.dataFim() != null;
    }

    public Page<RepasseDepartamento> buscarRepassesDepartamento(Long id, Pageable pageable, FiltroRepasseDepartamento filtro) {
        if(isFiltroDep(filtro)) {
            Specification<RepasseDepartamento> spec = RepasseDepartamentoRepository.repasseDepartamentoSpec(filtro);
            return repasseDepartamentoRepository.findAll(spec, pageable);
        }
        return repasseDepartamentoRepository.findAllByDepartamento_Id(id, pageable);
    }

    private boolean isFiltroDep(FiltroRepasseDepartamento filtro) {
        return filtro.dataInicio() != null||
                filtro.dataFim() != null;
    }
}
