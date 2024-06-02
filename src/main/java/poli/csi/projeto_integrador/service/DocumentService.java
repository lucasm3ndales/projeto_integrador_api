package poli.csi.projeto_integrador.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import poli.csi.projeto_integrador.dto.request.DocumentDto;
import poli.csi.projeto_integrador.model.Document;
import poli.csi.projeto_integrador.model.Procedure;
import poli.csi.projeto_integrador.repository.DocumentRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

@Service
@AllArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;

    @Transactional
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

            try {

                if(d.document().contains("base64,")){
                    String content = d.document().split("base64,")[1];

                    byte[] docContent = Base64.getDecoder().decode(content);

                    Document doc = Document.builder()
                            .name(d.name())
                            .type(type)
                            .extension(extension)
                            .createdAt(generateTimestamp(timezone))
                            .doc(docContent)
                            .procedure(procedure)
                            .build();

                            documents.add(doc);

                            documentRepository.save(doc);
                }
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Erro ao decodificar documento!");
            }
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
