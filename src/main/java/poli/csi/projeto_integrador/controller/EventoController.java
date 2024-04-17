package poli.csi.projeto_integrador.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poli.csi.projeto_integrador.dto.filter.FiltroEvento;
import poli.csi.projeto_integrador.dto.request.AlterarEventoDto;
import poli.csi.projeto_integrador.dto.request.AlterarStatusEventoDto;
import poli.csi.projeto_integrador.dto.request.SalvarEventoDto;
import poli.csi.projeto_integrador.dto.request.TramiteEventoDto;
import poli.csi.projeto_integrador.dto.response.EventoStatusResDto;
import poli.csi.projeto_integrador.model.Evento;
import poli.csi.projeto_integrador.service.EventoService;

@RestController
@RequestMapping("/evento")
@AllArgsConstructor
public class EventoController {
    private final EventoService eventoService;

    @PostMapping("/salvar")
    public ResponseEntity<String> salvarEvento(
            @RequestHeader(name = "timezone") String timezone,
            @Valid @RequestBody SalvarEventoDto dto
    ) {
        boolean res = eventoService.salvarEvento(dto, timezone);
        if(res) {
            return ResponseEntity.ok("Evento salvo com sucesso!");
        }
        return ResponseEntity.internalServerError().body("Erro ao salvar evento!");
    }

    @PutMapping("/alterar")
    public ResponseEntity<String> alterarEvento(
            @Valid @RequestBody AlterarEventoDto dto,
            @RequestHeader(value = "timezone") String timezone
    ) {
        boolean res = eventoService.alterarEvento(dto, timezone);
        if(res) {
            return ResponseEntity.ok("Dados do evento alterados com sucesso!");
        }
        return ResponseEntity.internalServerError().body("Erro alterar dados do evento!");
    }

    @PutMapping("/alterar/status")
    public ResponseEntity<?> alterarStatusEvento(
            @Valid @RequestBody AlterarStatusEventoDto dto,
            @RequestHeader(value = "timezone") String timezone
    ) {
        EventoStatusResDto res = eventoService.alterarStatusEvento(dto, timezone);
        if(res != null) {
            return ResponseEntity.ok(res);
        }
        return ResponseEntity.internalServerError().body("Erro ao alterar status do evento!");
    }

    @PutMapping("/tramitar")
    public ResponseEntity<?> tramitarEvento(
            @Valid @RequestBody TramiteEventoDto dto,
            @RequestHeader(value = "timezone") String timezone
    ) {
        boolean res = eventoService.tramitarEvento(dto, timezone);
        if(res) {
            return ResponseEntity.ok("Evento trâmitado com sucesso!");
        }
        return ResponseEntity.internalServerError().body("Erro ao tramitar evento!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarEvento(
            @PathVariable("id") Long id
    ) {
        if(id != null) {
            Evento evento = eventoService.buscarEvento(id);
            if(evento != null) {
                return ResponseEntity.ok(evento);
            }
            return ResponseEntity.notFound().build();
        }
        return  ResponseEntity.badRequest().body("Id do evento nulo!");
    }

    @GetMapping("/eventos/{id}")
    public ResponseEntity<?> buscarEventos(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @PathVariable("id") Long id,
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "tipo", required = false) String tipo,
            @RequestParam(value = "periodicidade", required = false) String periodicidade,
            @RequestParam(value = "dataInicio", required = false) String dataInicio,
            @RequestParam(value = "dataFim", required = false) String dataFim,
            @RequestParam(value = "arquivado") Boolean arquivado,
            @RequestParam(value = "status", required = false) String status

    ) {
        if(id != null) {
            FiltroEvento filtro = new FiltroEvento(nome, tipo, periodicidade, dataInicio, dataFim, arquivado, status);
            Page<Evento> res = eventoService.buscarEventos(id, pageable, filtro);
            if(res.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(res);
        }
        return ResponseEntity.badRequest().body("Id de usuário nulo!");
    }

}
