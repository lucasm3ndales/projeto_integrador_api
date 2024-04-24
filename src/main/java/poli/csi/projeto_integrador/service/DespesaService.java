package poli.csi.projeto_integrador.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import poli.csi.projeto_integrador.dto.filtro.DespesaFiltro;
import poli.csi.projeto_integrador.dto.request.DespesaAlterarDto;
import poli.csi.projeto_integrador.dto.request.DespesaSalvarDto;
import poli.csi.projeto_integrador.exception.CustomException;
import poli.csi.projeto_integrador.model.Despesa;
import poli.csi.projeto_integrador.repository.DespesaRepository;

@Service
@AllArgsConstructor
public class DespesaService {
    private final DespesaRepository despesaRepository;

    @Transactional
    public boolean salvar(DespesaSalvarDto dto) {
        Despesa isExist = despesaRepository.findDespesaByNomeEqualsIgnoreCase(dto.nome().trim())
                .orElse(null);

        if (isExist != null) {
            throw new CustomException("Despesa já cadastrada no sistema!");
        }

        Despesa.TipoDespesa tipo = null;
        try {
            tipo = Despesa.TipoDespesa.valueOf(dto.tipo().trim());
        } catch (IllegalArgumentException ex) {
            throw ex;
        }

        Despesa despesa = new Despesa();
        despesa.setNome(dto.nome().toLowerCase().trim());
        despesa.setTipo(tipo);

        despesaRepository.save(despesa);
        return true;
    }

    @Transactional
    public boolean alterar(DespesaAlterarDto dto) {
        Despesa isExist = despesaRepository.findDespesaByNomeEqualsIgnoreCase(dto.nome().trim())
                .orElse(null);

        if (isExist != null) {
            throw new CustomException("Despesa já cadastrada no sistema!");
        }

        Despesa.TipoDespesa tipo = null;
        try {
            tipo = Despesa.TipoDespesa.valueOf(dto.tipo().trim());
        } catch (IllegalArgumentException ex) {
            throw ex;
        }

        Despesa despesa = despesaRepository.findById(dto.id())
                .orElseThrow(() -> new EntityNotFoundException("Despesa não encontrada!"));

        despesa.setNome(dto.nome().toLowerCase().trim());
        despesa.setTipo(tipo);

        despesaRepository.save(despesa);
        return true;
    }

    public Despesa buscarDespesa(Long id) {
        return despesaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Despesa não encontrada!"));
    }

    public Page<Despesa> buscarDespesas(Pageable pageable, DespesaFiltro filtro) {
        if(isFiltro(filtro)) {
            Specification<Despesa> spec = DespesaRepository.specDespesa(filtro);
            return despesaRepository.findAll(spec, pageable);
        }
        return despesaRepository.findAll(pageable);
    }

    private boolean isFiltro(DespesaFiltro f) {
        return f.nome() == null || f.nome().trim().isEmpty() ||
                f.tipo() == null || f.tipo().trim().isEmpty();
    }
}
