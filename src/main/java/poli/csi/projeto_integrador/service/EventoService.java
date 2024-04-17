package poli.csi.projeto_integrador.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import poli.csi.projeto_integrador.dto.filter.FiltroEvento;
import poli.csi.projeto_integrador.dto.request.AlterarEventoDto;
import poli.csi.projeto_integrador.dto.request.AlterarStatusEventoDto;
import poli.csi.projeto_integrador.dto.request.SalvarEventoDto;
import poli.csi.projeto_integrador.dto.request.TramiteEventoDto;
import poli.csi.projeto_integrador.dto.response.EventoStatusResDto;
import poli.csi.projeto_integrador.exception.CustomException;
import poli.csi.projeto_integrador.model.*;
import poli.csi.projeto_integrador.repository.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Set;

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

        Tramite.StatusTramite status = null;
        status = Tramite.StatusTramite.valueOf(dto.statusTramite());

        if(status == null) {
            throw new CustomException("Status do trâmite inválido!");
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
        evento.setAporteDep(BigDecimal.ZERO);
        evento.setAporteReit(BigDecimal.ZERO);
        eventoRepository.save(evento);

        boolean res1 = despesaService.adicionarDespesaEvento(dto.despesas(), evento, timezone);
        if (!res1) {
            return false;
        }

        boolean res2 = documentoService.salvarDocumentos(dto.documentos(), evento, timezone, servidor);
        if (!res2) {
            return false;
        }

        boolean res3 = tramiteService.tramitar(evento, servidor, departamento, status, timezone);
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
    public boolean alterarEvento(AlterarEventoDto dto, String timezone) {
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

        Evento evento = eventoRepository.findById(dto.eventoId())
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado!"));


        Tramite ultimoTramite = buscarUltimoTramite(evento.getTramites());
        Usuario usuario = ultimoTramite.getDestino();

        if(!usuario.getId().equals(dto.usuarioId())) {
            throw new CustomException("Esse usuário não tem permissão para alterar o evento!");
        }

        evento.getEndereco().setPais(dto.pais().toLowerCase().trim());
        evento.getEndereco().setEstado(dto.estado().toLowerCase().trim());
        evento.getEndereco().setCidade(dto.cidade().toLowerCase().trim());
        evento.getEndereco().setBairro(dto.bairro().toLowerCase().trim());
        evento.getEndereco().setRua(dto.rua().toLowerCase().trim());
        evento.getEndereco().setNumero(dto.numero().toLowerCase().trim());
        evento.getEndereco().setComplemento(dto.complemento());

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
        eventoRepository.save(evento);

        boolean res1 = despesaService.alterarDespesaEvento(dto.despesas(), evento, timezone);
        if (!res1) {
            return false;
        }

        boolean res2 = documentoService.salvarDocumentos(dto.documentos(), evento, timezone, usuario);
        if (!res2) {
            return false;
        }

        boolean res3 = tramiteService.tramitar(evento, usuario, ultimoTramite.getOrigem(), ultimoTramite.getStatus(), timezone);
        if (!res3) {
            return false;
        }

        return true;
    }

    private Tramite buscarUltimoTramite(Set<Tramite> tramites) {
        Timestamp ultimoTimestamp = null;
        Tramite ultimoTramite = null;
        for (Tramite t : tramites) {
            if (ultimoTimestamp == null || t.getCriadoEm().after(ultimoTimestamp)) {
                ultimoTimestamp = t.getCriadoEm();
                ultimoTramite = t;
            }
        }
        return ultimoTramite;
    }


    @Transactional
    public EventoStatusResDto alterarStatusEvento(AlterarStatusEventoDto dto, String timezone) {
        Evento evento = eventoRepository.findById(dto.eventoId())
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado!"));

        Evento.StatusEvento statusEvento = null;
        statusEvento = Evento.StatusEvento.valueOf(dto.status());

        if(statusEvento == null || statusEvento == Evento.StatusEvento.PENDENTE) {
            throw new CustomException("Status do evento inválido!");
        }

        evento.setStatus(statusEvento);

        Usuario origem = usuarioRepository.findById(dto.origemId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário remetente não encontrado!"));

        if(!(origem.getRole() == Usuario.TipoUsuario.DEPARTAMENTO || origem.getRole() == Usuario.TipoUsuario.REITORIA)) {
            throw new CustomException("Usuário não autorizado!");
        }

        Tramite primeiroTramite = null;
        Instant dataAntiga = null;
        for(Tramite tramite : evento.getTramites()) {
            Instant dataTempo = tramite.getCriadoEm().toInstant();
            if (dataAntiga == null || dataTempo.isBefore(dataAntiga)) {
                dataAntiga = dataTempo;
                primeiroTramite = tramite;
            }
        }

        if(primeiroTramite == null) {
            throw new CustomException("Erro ao validar tramites do evento!");
        }

        Usuario destino = primeiroTramite.getOrigem();

        boolean res = tramiteService.tramitar(evento, origem, destino, Tramite.StatusTramite.ENCERRADO, timezone);

        if(!res) {
            throw new CustomException("Erro ao alterar status do evento!");
        }

        if(origem.getRole() == Usuario.TipoUsuario.DEPARTAMENTO) {

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
        }

        if(origem.getRole() == Usuario.TipoUsuario.REITORIA) {
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
        }

        eventoRepository.save(evento);

        return new EventoStatusResDto(evento.getStatus().name(), "Status do evento alterado com sucesso!");
    }

    @Transactional
    public boolean tramitarEvento(TramiteEventoDto dto, String timezone) {
        Evento evento = eventoRepository.findById(dto.eventoId())
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado!"));


        if(evento.getStatus() != Evento.StatusEvento.PENDENTE) {
            throw new CustomException("Status do evento inválido!");
        }

        Usuario origem = usuarioRepository.findById(dto.origemId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário remetente não encontrado!"));

        Usuario destino = usuarioRepository.findById(dto.destinoId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário destinatario não encontrado!"));

        if(origem.getRole() == Usuario.TipoUsuario.DEPARTAMENTO && destino.getRole() == Usuario.TipoUsuario.REITORIA) {
            if(evento.getCusto().compareTo(dto.aporte()) == 0 || evento.getCusto().compareTo(dto.aporte()) == -1) {
                throw new CustomException("Aporte inválido para tramitação do evento!");
            }
            evento.setAporteDep(dto.aporte());
            evento.setAporteReit(BigDecimal.ZERO);
        } else if(origem.getRole() == Usuario.TipoUsuario.DEPARTAMENTO && destino.getRole() == Usuario.TipoUsuario.SERVIDOR) {
            if(!(dto.aporte().compareTo(BigDecimal.ZERO) == 0)) {
                throw new CustomException("Aporte inválido para tramitação do evento!");
            }
            evento.setAporteDep(BigDecimal.ZERO);
            evento.setAporteReit(BigDecimal.ZERO);
        }

        boolean res = tramiteService.tramitar(evento, origem, destino, Tramite.StatusTramite.EM_ABERTO, timezone);

        if(!res) {
            throw new CustomException("Erro ao alterar status do evento!");
        }

        eventoRepository.save(evento);
        return true;
    }

    public Evento buscarEvento(Long id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado!"));
    }


    public Page<Evento> buscarEventos(Long id, Pageable pageable, FiltroEvento filtro) {
        if(isFilter(filtro)) {
            Specification<Evento> spec = EventoRepository.eventoSpec(id, filtro);
            return eventoRepository.findAll(spec, pageable);
        }
        return eventoRepository.buscarTodosEventos(id, pageable, filtro.arquivado());
    }

    boolean isFilter(FiltroEvento filtro) {
        return filtro.nome() != null ||
                filtro.tipo() != null ||
                filtro.periodicidade() != null ||
                filtro.status() != null ||
                filtro.dataInicio() != null ||
                filtro.dataFim() != null &&
                filtro.arquivado() != null;
    }
}
