package poli.csi.projeto_integrador.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import poli.csi.projeto_integrador.dto.request.AlterarEventoDto;
import poli.csi.projeto_integrador.dto.request.AlterarStatusEventoDto;
import poli.csi.projeto_integrador.dto.request.SalvarEventoDto;
import poli.csi.projeto_integrador.dto.response.EventoStatusResDto;
import poli.csi.projeto_integrador.exception.CustomException;
import poli.csi.projeto_integrador.model.*;
import poli.csi.projeto_integrador.repository.*;

import java.math.BigDecimal;
import java.time.Instant;
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
    private final UsuarioRepository usuarioRepository;

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

    //TODO: Pensar se será inportante alterar o evento
    @Transactional
    public boolean alterarEvento(AlterarEventoDto dto) {
        return true;
    }

    @Transactional
    public EventoStatusResDto alterarStatusEvento(AlterarStatusEventoDto dto, String timezone) {
        Evento evento = eventoRepository.findById(dto.eventoId())
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado!"));

        Evento.StatusEvento status = null;
        status = Evento.StatusEvento.valueOf(dto.status());

        if((status == null) || (status == Evento.StatusEvento.PENDENTE)) {
            throw new CustomException("Status do evento inválido!");
        }

        evento.setStatus(status);

        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado!"));

        if(usuario.getRole() == Usuario.TipoUsuario.DEPARTAMENTO) {
            Tramite primeiroTramite = null;
            Instant dataAntiga = null;
            for(Tramite tramite : evento.getTramites()) {
                Instant dataTempo = tramite.getDataTempo().toInstant();
                if (dataAntiga == null || dataTempo.isBefore(dataAntiga)) {
                    dataAntiga = dataTempo;
                    primeiroTramite = tramite;
                }
            }

            if(primeiroTramite == null) {
                throw new CustomException("Erro ao validar tramites do evento!");
            }

            Usuario destino = primeiroTramite.getOrigem();

            boolean res = tramiteService.retornarTramite(evento, usuario, destino, timezone);

            if(!res) {
                throw new CustomException("Erro ao alterar status do evento!");
            }

            if(evento.getStatus() == Evento.StatusEvento.ACEITO) {
                if(evento.getCusto().compareTo(dto.aporte()) == 0) {
                    evento.setAporteDep(dto.aporte());
                    evento.setAporteReit(BigDecimal.ZERO);
                }
                throw new CustomException("Aporte insuficiente para o custo do evento!");
            }

            if(evento.getStatus() == Evento.StatusEvento.RECUSADO) {
                evento.setAporteDep(BigDecimal.ZERO);
                evento.setAporteReit(BigDecimal.ZERO);
            }

            if(evento.getStatus() == Evento.StatusEvento.TRAMITADO) {
                if(evento.getCusto().compareTo(dto.aporte()) == 0 || evento.getCusto().compareTo(dto.aporte()) == -1) {
                    throw new CustomException("Aporte inválido para tramitação do evento!");
                }
                evento.setAporteDep(dto.aporte());
                evento.setAporteReit(BigDecimal.ZERO);
            }

            return new EventoStatusResDto(evento.getStatus().name(), "Status do evento alterado com sucesso!");
        } else if(usuario.getRole() == Usuario.TipoUsuario.REITORIA) {
            Tramite primeiroTramite = null;
            Instant dataAntiga = null;
            for(Tramite tramite : evento.getTramites()) {
                Instant dataTempo = tramite.getDataTempo().toInstant();
                if (dataAntiga == null || dataTempo.isBefore(dataAntiga)) {
                    dataAntiga = dataTempo;
                    primeiroTramite = tramite;
                }
            }

            if(primeiroTramite == null) {
                throw new CustomException("Erro ao validar tramites do evento!");
            }

            Usuario destino = primeiroTramite.getOrigem();

            boolean res = tramiteService.retornarTramite(evento, usuario, destino, timezone);

            if(!res) {
                throw new CustomException("Erro ao alterar status do evento!");
            }

            if(evento.getStatus() == Evento.StatusEvento.ACEITO) {
                BigDecimal diferenca = evento.getCusto().subtract(evento.getAporteDep());
                if(diferenca.compareTo(dto.aporte()) == 0) {
                    evento.setAporteReit(dto.aporte());
                }
                throw new CustomException("Aporte insuficiente para o custo do evento!");
            }

            if(evento.getStatus() == Evento.StatusEvento.RECUSADO) {
                evento.setAporteReit(BigDecimal.ZERO);
            }

            return new EventoStatusResDto(evento.getStatus().name(), "Status do evento alterado com sucesso!");
        } else {
            throw new CustomException("Usuário não autorizado!");
        }

    }

    public Evento buscarEvento(Long id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado!"));
    }
}
