package poli.csi.projeto_integrador.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import poli.csi.projeto_integrador.model.Evento;
import poli.csi.projeto_integrador.model.Tramite;
import poli.csi.projeto_integrador.model.Usuario;
import poli.csi.projeto_integrador.repository.TramiteRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;

@Service
@AllArgsConstructor
public class TramiteService {
    private final TramiteRepository tramiteRepository;

    public boolean salvarTramite(Evento evento, Usuario origem, Usuario destino, String timezone) {
        Tramite tramite = new Tramite();
        tramite.setStatus(Tramite.StatusTramite.EM_TRAMITE);
        tramite.setDataTempo(gerarTimestamp(timezone));
        tramite.setEvento(evento);
        tramite.setOrigem(origem);
        tramite.setDestino(destino);
        tramiteRepository.save(tramite);
        return true;
    }

    public boolean salvarTramite(Long evento, Long origem, Long destino, String timezone) {
        return true;
    }

    private Timestamp gerarTimestamp(String timezone) {
        Instant i = Instant.now().atZone(ZoneId.of(timezone)).toInstant();
        return Timestamp.from(i);
    }
}
