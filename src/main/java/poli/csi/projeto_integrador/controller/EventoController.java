package poli.csi.projeto_integrador.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poli.csi.projeto_integrador.dto.request.AlterarEventoDto;
import poli.csi.projeto_integrador.dto.request.AlterarStatusEventoDto;
import poli.csi.projeto_integrador.dto.request.SalvarEventoDto;
import poli.csi.projeto_integrador.dto.response.EventoStatusResDto;
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
            @Valid @RequestBody AlterarEventoDto dto
    ) {
        boolean res = eventoService.alterarEvento(dto);
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

}
