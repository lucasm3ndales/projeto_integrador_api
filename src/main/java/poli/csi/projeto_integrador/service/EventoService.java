package poli.csi.projeto_integrador.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import poli.csi.projeto_integrador.dto.request.AlterarEventoDto;
import poli.csi.projeto_integrador.dto.request.SalvarEventoDto;
import poli.csi.projeto_integrador.exception.CustomException;
import poli.csi.projeto_integrador.model.Departamento;
import poli.csi.projeto_integrador.model.Endereco;
import poli.csi.projeto_integrador.model.Evento;
import poli.csi.projeto_integrador.model.Servidor;
import poli.csi.projeto_integrador.repository.DepartamentoRepository;
import poli.csi.projeto_integrador.repository.EventoRepository;
import poli.csi.projeto_integrador.repository.ReitoriaRepository;
import poli.csi.projeto_integrador.repository.ServidorRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
@AllArgsConstructor
public class EventoService {
    private final EventoRepository eventoRepository;
    private final ServidorRepository servidorRepository;
    private final DepartamentoRepository departamentoRepository;
    private final ReitoriaRepository reitoriaRepository;
    private final TramiteService tramiteService;
    private final DocumentoService documentoService;
    private final DespesaService despesaService;

    @Transactional
    public boolean salvarEvento(SalvarEventoDto dto, String timezone) {
        Servidor servidor = servidorRepository.findById(dto.servidorId())
                .orElseThrow(() -> new EntityNotFoundException("Servidor não encontrado!"));

        Departamento departamento = departamentoRepository.findById(dto.departamentoId())
                .orElseThrow(() -> new EntityNotFoundException("Departamento não encontrado!"));

        Evento.TipoEvento tipo = null;
        tipo = Evento.TipoEvento.valueOf(dto.tipo().trim());

        if(tipo == null) {
            throw new CustomException("Tipo do evento inválido!");
        }

        Evento.Periodicidade periodicidade = null;
        periodicidade = Evento.Periodicidade.valueOf(dto.periodicidade().trim());

        if(periodicidade == null) {
            throw new CustomException("Periodicidade do evento inválida!");
        }

        Endereco endereco = new Endereco(
                dto.pais().toLowerCase().trim(),
                dto.estado().toLowerCase().trim(),
                dto.cidade().toLowerCase().trim(),
                dto.bairro().toLowerCase().trim(),
                dto.rua().toLowerCase().trim(),
                dto.numero().toLowerCase().trim(),
                dto.complemento()
        );
        Evento evento = new Evento();
        evento.setEndereco(endereco);
        evento.setNome(dto.nome().trim());
        evento.setTipo(tipo);
        evento.setPeriodicidade(periodicidade);
        evento.setDataInicio(convertToLocalDate(dto.dataInicio()));
        evento.setDataFim(convertToLocalDate(dto.dataFim()));
        evento.setDataIda(convertToLocalDate(dto.dataIda()));
        evento.setDataVolta(convertToLocalDate(dto.dataVolta()));
        evento.setObjetivo(dto.objetivo().trim());
        evento.setParticipantes(dto.participantes());
        evento.setCusto(dto.custo());
        evento.setArquivado(false);
        evento.setStatus(Evento.StatusEvento.PENDENTE);
        eventoRepository.save(evento);

        boolean res1 = despesaService.salvarDespesas(dto.despesas(), evento, timezone);
        if (!res1) {
            return false;
        }

        boolean res2 = documentoService.salvarDocumentos(dto.documentos(), evento);
        if (!res2) {
            return false;
        }

        boolean res3 = tramiteService.salvarTramite(evento, servidor, departamento, timezone);
        if (!res3) {
            return false;
        }

        return true;
    }

    private LocalDate convertToLocalDate(String dateString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(dateString.trim(), formatter);
        } catch (DateTimeParseException ex) {
            throw ex;
        }
    }

    @Transactional
    public boolean alterarEvento(AlterarEventoDto dto) {

    }
}
