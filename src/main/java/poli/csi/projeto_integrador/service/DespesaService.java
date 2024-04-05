package poli.csi.projeto_integrador.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import poli.csi.projeto_integrador.dto.request.DespesaDto;
import poli.csi.projeto_integrador.exception.CustomException;
import poli.csi.projeto_integrador.model.Despesa;
import poli.csi.projeto_integrador.model.DespesaEvento;
import poli.csi.projeto_integrador.model.Evento;
import poli.csi.projeto_integrador.repository.DespesaEventoRepository;
import poli.csi.projeto_integrador.repository.DespesaRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Set;

@Service
@AllArgsConstructor
public class DespesaService {
    private final DespesaRepository despesaRepository;
    private final DespesaEventoRepository despesaEventoRepository;

    public boolean salvarDespesas(Set<DespesaDto> despesasDto, Evento evento, String timezone) {
        despesasDto.forEach(dto -> {

            Despesa.TipoDespesa tipo = null;
            tipo = Despesa.TipoDespesa.valueOf(dto.tipo().trim());
            if(tipo == null) {
                throw new CustomException("Tipo de despesa inválido!");
            }

            Despesa despesa = new Despesa();
            despesa.setNome(dto.nome().trim());
            despesa.setTipo(tipo);
            despesaRepository.save(despesa);

            DespesaEvento despesaEvento = new DespesaEvento();
            despesaEvento.setDespesa(despesa);
            despesaEvento.setEvento(evento);
            despesaEvento.setDataTempo(gerarTimestamp(timezone));
            despesaEvento.setValor(dto.valor());
            despesaEvento.setJustificativa(dto.justificativa());

            despesa.getDespesaEventos().add(despesaEvento);

            despesaRepository.save(despesa);

        });
        return true;
    }

    private Timestamp gerarTimestamp(String timezone) {
        Instant i = Instant.now().atZone(ZoneId.of(timezone)).toInstant();
        return Timestamp.from(i);
    }

}
