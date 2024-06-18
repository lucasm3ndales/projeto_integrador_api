package poli.csi.projeto_integrador.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poli.csi.projeto_integrador.dto.request.ProcedureDto;
import poli.csi.projeto_integrador.service.ProcedureService;

@RestController
@RequestMapping("/procedure")
@AllArgsConstructor
public class ProcedureController {
    private final ProcedureService procedureService;

    @PostMapping
    public ResponseEntity<String> addProcedure(@RequestBody ProcedureDto dto, @RequestHeader("timezone") String timezone) {
        boolean res = procedureService.process(dto, timezone);
        if(res) {
            return ResponseEntity.ok().body("Evento tramitado com sucesso!");

        }
        return ResponseEntity.badRequest().body("Erro ao efetuar trâmite!");
    }
}
