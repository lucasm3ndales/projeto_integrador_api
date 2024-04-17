package poli.csi.projeto_integrador.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import poli.csi.projeto_integrador.dto.filter.FiltroDespesa;
import poli.csi.projeto_integrador.dto.request.AdicionarDespesaDto;
import poli.csi.projeto_integrador.dto.request.AlterarDespesaDto;
import poli.csi.projeto_integrador.dto.request.SalvarDespesaDto;
import poli.csi.projeto_integrador.model.Despesa;
import poli.csi.projeto_integrador.model.DespesaEvento;
import poli.csi.projeto_integrador.model.Evento;
import poli.csi.projeto_integrador.repository.DespesaRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Set;

@Service
@AllArgsConstructor
public class DespesaService {
    private final DespesaRepository despesaRepository;

    @Transactional
    public boolean salvarDespesa(SalvarDespesaDto dto) {
        Despesa.TipoDespesa tipo = null;
        try {
            tipo = Despesa.TipoDespesa.valueOf(dto.tipo().trim());
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Tipo de despesa inválido!");
        }

        Despesa despesa = new Despesa();
        despesa.setNome(dto.nome().trim());
        despesa.setTipo(tipo);
        despesaRepository.save(despesa);
        return true;
    }

    @Transactional
    public boolean alterarDespesa(AlterarDespesaDto dto) {
        Despesa despesa = despesaRepository.findById(dto.id())
                .orElseThrow(() -> new EntityNotFoundException("Despesa não encontrada!"));

        Despesa.TipoDespesa tipo = null;
        try {
            tipo = Despesa.TipoDespesa.valueOf(dto.tipo().trim());
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("Tipo de despesa inválido!");
        }

        despesa.setNome(dto.nome().trim());
        despesa.setTipo(tipo);
        despesaRepository.save(despesa);
        return true;
    }

    public boolean adicionarDespesaEvento(Set<AdicionarDespesaDto> despesasDto, Evento evento, String timezone) {
        despesasDto.forEach(dto -> {
            Despesa despesa = despesaRepository.findById(dto.id())
                    .orElseThrow(() -> new EntityNotFoundException("Despesa não encontrada!"));

            DespesaEvento despesaEvento = new DespesaEvento();
            despesaEvento.setEvento(evento);
            despesaEvento.setDespesa(despesa);
            despesaEvento.setValor(dto.valor());
            despesaEvento.setJustificativa(dto.justificativa().trim());
            despesaEvento.setCriadoEm(gerarTimestamp(timezone));

            despesaRepository.save(despesa);
        });
        return true;
    }


    public boolean alterarDespesaEvento(Set<AdicionarDespesaDto> despesasDto, Evento evento, String timezone) {
        despesasDto.forEach(dto -> {
            evento.getDespesaEventos().forEach(i -> {
                if(i.getDespesa().getId().equals(dto.id())) {
                    if(!(i.getValor().compareTo(dto.valor()) == 0 || i.getJustificativa().compareTo(dto.justificativa()) == 0)) {
                        i.setAtualizadoEm(gerarTimestamp(timezone));
                        i.setJustificativa(dto.justificativa().trim());
                        i.setValor(dto.valor());
                        despesaRepository.save(i.getDespesa());
                    }
                } else {
                    Despesa despesa = despesaRepository.findById(dto.id())
                            .orElseThrow(() -> new EntityNotFoundException("Despesa não encontrada!"));

                    DespesaEvento despesaEvento = new DespesaEvento();
                    despesaEvento.setEvento(evento);
                    despesaEvento.setDespesa(despesa);
                    despesaEvento.setValor(dto.valor());
                    despesaEvento.setJustificativa(dto.justificativa().trim());
                    despesaEvento.setCriadoEm(gerarTimestamp(timezone));

                    despesaRepository.save(despesa);
                }
            });
        });
        return true;
    }

    public Page<Despesa> buscarDespesas(Pageable pageable, FiltroDespesa filtro) {
        if(isFiltro(filtro)) {
            Specification<Despesa> spec = DespesaRepository.despesaSpec(filtro);
            return despesaRepository.findAll(spec, pageable);
        }
        return despesaRepository.findAll(pageable);
    }

    private boolean isFiltro(FiltroDespesa filtro) {
        return filtro.nome() != null && !filtro.nome().isEmpty() ||
                filtro.tipo() != null && !filtro.tipo().isEmpty();
    }

    private Timestamp gerarTimestamp(String timezone) {
        Instant i = Instant.now().atZone(ZoneId.of(timezone)).toInstant();
        return Timestamp.from(i);
    }


}
