package poli.csi.projeto_integrador.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import poli.csi.projeto_integrador.dto.request.DocumentDto;
import poli.csi.projeto_integrador.dto.request.ProcedureDto;
import poli.csi.projeto_integrador.exception.CustomException;
import poli.csi.projeto_integrador.model.Event;
import poli.csi.projeto_integrador.model.Procedure;
import poli.csi.projeto_integrador.model.User;
import poli.csi.projeto_integrador.repository.EventRepository;
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
    private final EventRepository eventRepository;
    private final ProcedureRepository procedureRepository;
    private final UserRepository userRepository;
    private final DocumentService documentService;

    @Transactional
    public boolean process(ProcedureDto dto, String timezone) {
        User origin = userRepository.findById(dto.originId()).orElseThrow(() -> new EntityNotFoundException("Usuário origem não encontrado!"));
        User destiny = userRepository.findByUnityId(dto.destinyId()).orElseThrow(() -> new EntityNotFoundException("Usuário destino não encontrado!"));
        Event event = eventRepository.findById(dto.eventId()).orElseThrow(() -> new EntityNotFoundException("Evento não encontrado!"));

        Procedure lastProcedure = getLastProcedure(event);

        if(!lastProcedure.getDestiny().getId().equals(origin.getId())) {
            throw new CustomException("Usuário não autorizado a realizar modificações!");
        }

        Procedure procedure = Procedure.builder()
                .origin(origin)
                .destiny(destiny)
                .createdAt(generateTimestamp(timezone))
                .event(event)
                .build();

        procedureRepository.save(procedure);
        return true;
    }

    @Transactional
    public boolean process(Long o, User destiny, List<DocumentDto> documents, Event event, String timezone) {
        User origin = userRepository.findById(o).orElseThrow(() -> new EntityNotFoundException("Usuário origem não encontrado!"));

        Procedure procedure = Procedure.builder()
                .origin(origin)
                .destiny(destiny)
                .createdAt(generateTimestamp(timezone))
                .event(event)
                .build();

        procedureRepository.save(procedure);

        if(!documents.isEmpty()) {
            boolean resDocuments = documentService.addDocumentsToProcedure(documents, procedure, timezone);
            if(!resDocuments) {
                throw new CustomException("Erro ao anexar documentos ao trâmite!");
            }
        }

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

    private Procedure getLastProcedure(Event event) {
        return event.getProcedures().stream()
                .max(Comparator.comparing(Procedure::getCreatedAt))
                .orElseThrow(() -> new EntityNotFoundException("Não há trâmites ligados ao evento!"));
    }


}
