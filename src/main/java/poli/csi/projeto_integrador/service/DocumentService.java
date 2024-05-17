package poli.csi.projeto_integrador.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import poli.csi.projeto_integrador.dto.request.DocumentDto;
import poli.csi.projeto_integrador.model.Document;
import poli.csi.projeto_integrador.model.Procedure;
import poli.csi.projeto_integrador.repository.DocumentRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class DocumentService {
    DocumentRepository documentRepository;

    public boolean addDocumentsToProcedure(List<DocumentDto> docs, Procedure procedure, String timezone) {
        Set<Document> documents = new HashSet<>();
        docs.forEach(d -> {
            Document.DocumentType type = null;
            Document.Extensions extension = null;

            try {
                type = Document.DocumentType.valueOf(d.type());
                extension = Document.Extensions.valueOf(d.extension());
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Tipo ou extensão de documento inválida!");
            }

            Document doc = Document.builder()
                    .name(d.name())
                    .type(type)
                    .extension(extension)
                    .createdAt(generateTimestamp(timezone))
                    .doc(d.document())
                    .procedure(procedure)
                    .build();

            documents.add(doc);
        });

        documentRepository.saveAll(documents);
        return true;
    }

    private Timestamp generateTimestamp(String timezone) {
        Instant instant = Instant.now();
        ZoneId zoneId = ZoneId.of(timezone);
        ZonedDateTime zonedDateTime = instant.atZone(zoneId);
        ZonedDateTime utcDateTime = zonedDateTime.withZoneSameInstant(ZoneOffset.UTC);
        return Timestamp.from(utcDateTime.toInstant());
    }

    public Document getDocument(Long id) {
        return documentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Documento não encontrado!"));
    }


}
