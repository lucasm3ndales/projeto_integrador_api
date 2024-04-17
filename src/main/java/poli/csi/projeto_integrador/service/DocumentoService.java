package poli.csi.projeto_integrador.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import poli.csi.projeto_integrador.dto.request.SalvarDocumentoDto;
import poli.csi.projeto_integrador.exception.CustomException;
import poli.csi.projeto_integrador.model.DocumentoEvento;
import poli.csi.projeto_integrador.model.Evento;
import poli.csi.projeto_integrador.model.Usuario;
import poli.csi.projeto_integrador.repository.DocumentoEventoRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Set;

@Service
@AllArgsConstructor
public class DocumentoService {
    private final DocumentoEventoRepository documentoEventoRepository;

    public boolean salvarDocumentos(Set<SalvarDocumentoDto> documentosDto, Evento evento, String timestamp, Usuario usuario) {
        documentosDto.forEach(dto -> {
            DocumentoEvento.TipoDocumento tipo = null;
            try{
                tipo = DocumentoEvento.TipoDocumento.valueOf(dto.tipo().trim());
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Tipo do documento inválido!");
            }

            DocumentoEvento documento = new DocumentoEvento();
            documento.setNome(dto.nome().trim());
            documento.setTipo(tipo);
            documento.setEvento(evento);
            documento.setCriadoEm(gerarTimestamp(timestamp));
            documento.setAnexadoPor(usuario);

            if (isBase64(dto.doc())) {
                byte[] docBytes = Base64.getDecoder().decode(dto.doc());
                documento.setDoc(docBytes);
            } else {
                throw new CustomException("O conteúdo do documento não está no formato válido!");
            }

            documentoEventoRepository.save(documento);
        });
        return true;
    }

    private boolean isBase64(String str) {
        try {
            Base64.getDecoder().decode(str);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    private Timestamp gerarTimestamp(String timezone) {
        Instant i = Instant.now().atZone(ZoneId.of(timezone)).toInstant();
        return Timestamp.from(i);
    }

    @Transactional
    public boolean deletarDocumento(Long documentoId, Usuario usuario) {
        DocumentoEvento documento = documentoEventoRepository.findById(documentoId)
                .orElseThrow(() -> new EntityNotFoundException("Documento não encontrado!"));

        if(documento.getAnexadoPor().getId().equals(usuario.getId())) {
            documentoEventoRepository.delete(documento);
        } else {
            throw new CustomException("Esse usuário não tem permissão para deletar o documento!");
        }
        return true;
    }
}
