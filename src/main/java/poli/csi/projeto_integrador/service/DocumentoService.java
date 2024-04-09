package poli.csi.projeto_integrador.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import poli.csi.projeto_integrador.dto.request.AlterarDocumentoDto;
import poli.csi.projeto_integrador.dto.request.SalvarDocumentoDto;
import poli.csi.projeto_integrador.exception.CustomException;
import poli.csi.projeto_integrador.model.DocumentoEvento;
import poli.csi.projeto_integrador.model.Evento;
import poli.csi.projeto_integrador.repository.DocumentoEventoRepository;

import java.util.Base64;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@AllArgsConstructor
public class DocumentoService {
    private final DocumentoEventoRepository documentoEventoRepository;

    public boolean salvarDocumentos(Set<SalvarDocumentoDto> documentosDto, Evento evento) {
        documentosDto.forEach(dto -> {
            DocumentoEvento.TipoDocumento tipo = null;
            tipo = DocumentoEvento.TipoDocumento.valueOf(dto.tipo().trim());
            if(tipo == null) {
                throw new CustomException("Tipo do documento inválido");
            }

            DocumentoEvento documento = new DocumentoEvento();
            documento.setNome(dto.nome().trim());
            documento.setTipo(tipo);
            documento.setEvento(evento);

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

    public boolean alterarDocumentos(Set<AlterarDocumentoDto> dto, Evento evento) {
        List<Long> ids = dto.stream().map(AlterarDocumentoDto::id).toList();

        evento.getDocumentos().clear();

        if(!dto.isEmpty()) {
            dto.forEach(i -> {
                DocumentoEvento.TipoDocumento tipo = null;
                tipo = DocumentoEvento.TipoDocumento.valueOf(i.tipo().trim());
                if(tipo == null) {
                    throw new CustomException("Tipo do documento inválido");
                }

                DocumentoEvento documento = new DocumentoEvento();
                documento.setNome(i.nome().trim());
                documento.setTipo(tipo);
                documento.setEvento(evento);
                documentoEventoRepository.save(documento);
            });
        }
        return true;
    }
}
