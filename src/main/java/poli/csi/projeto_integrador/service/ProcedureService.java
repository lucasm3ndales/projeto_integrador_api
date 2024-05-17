package poli.csi.projeto_integrador.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import poli.csi.projeto_integrador.dto.request.DocumentDto;
import poli.csi.projeto_integrador.model.Event;
import poli.csi.projeto_integrador.model.Procedure;
import poli.csi.projeto_integrador.model.User;
import poli.csi.projeto_integrador.repository.DocumentRepository;
import poli.csi.projeto_integrador.repository.ProcedureRepository;
import poli.csi.projeto_integrador.repository.UserRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class ProcedureService {
    ProcedureRepository procedureRepository;
    UserRepository userRepository;
    DocumentService documentService;

    public boolean process(Long o, Long d, List<DocumentDto> documents, Event event, String timezone) {
        User origin = userRepository.findById(o).orElseThrow(() -> new EntityNotFoundException("Usuário origem não encontrado!"));
        User destiny = userRepository.findById(d).orElseThrow(() -> new EntityNotFoundException("Usuário destino não encontrado!"));

        Procedure procedure = Procedure.builder()
                .origin(origin)
                .destiny(destiny)
                .createdAt(generateTimestamp(timezone))
                .event(event)
                .build();

        if(!documents.isEmpty()) {
            boolean resDocuments = documentService.addDocumentsToProcedure(documents, procedure, timezone);
        }

        procedureRepository.save(procedure);
        return true;
    }

    public boolean process(User origin, Event event, String timezone) {
        Procedure p = getFirstProcedure(event);

        User destiny = p.getOrigin();


        Procedure procedure = Procedure.builder()
                .origin(origin)
                .destiny(destiny)
                .createdAt(generateTimestamp(timezone))
                .event(event)
                .build();


        procedureRepository.save(procedure);
        return true;
    }

    private Timestamp generateTimestamp(String timezone) {
        Instant instant = Instant.now();
        ZoneId zoneId = ZoneId.of(timezone);
        ZonedDateTime zonedDateTime = instant.atZone(zoneId);
        ZonedDateTime utcDateTime = zonedDateTime.withZoneSameInstant(ZoneOffset.UTC);
        return Timestamp.from(utcDateTime.toInstant());
    }

    private Procedure getFirstProcedure(Event event) {
        return event.getProcedures().stream()
                .min(Comparator.comparing(Procedure::getCreatedAt))
                .orElseThrow(() -> new EntityNotFoundException("Não há trâmites ligados ao evento!"));
    }


}
