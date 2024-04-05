package poli.csi.projeto_integrador.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poli.csi.projeto_integrador.dto.filter.FiltroRepasseDepartamento;
import poli.csi.projeto_integrador.dto.filter.FiltroRepasseReitoria;
import poli.csi.projeto_integrador.model.RepasseDepartamento;
import poli.csi.projeto_integrador.model.RepasseReitoria;
import poli.csi.projeto_integrador.service.RepasseService;

@RestController
@RequestMapping("/repasse")
@AllArgsConstructor
public class RepasseController {
    private final RepasseService repasseService;

    @GetMapping("/reitoria/{id}")
    public ResponseEntity<?> buscarRepassesReitoria(
            @PathVariable("id") Long id,
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(name = "dataInicio", required = false) String dataInicio,
            @RequestParam(name = "dataFim", required = false) String dataFim
    ) {
        if(id != null) {
            FiltroRepasseReitoria filtro = new FiltroRepasseReitoria(dataInicio,  dataFim);
            Page<RepasseReitoria> res = repasseService.buscarRepassesReitoria(id,  pageable, filtro);
            if(res.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(res);
        }
        return ResponseEntity.badRequest().body("Id da reitoria nulo!");
    }

    @GetMapping("/departamento/{id}")
    public ResponseEntity<?> buscarRepassesDepartamento(
            @PathVariable("id") Long id,
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(name = "dataInicio", required = false) String dataInicio,
            @RequestParam(name = "dataFim", required = false) String dataFim
    ) {
        if(id != null) {
            FiltroRepasseDepartamento filtro = new FiltroRepasseDepartamento(dataInicio, dataFim);
            Page<RepasseDepartamento> res = repasseService.buscarRepassesDepartamento(id, pageable, filtro);
            if(res.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(res);
        }
        return ResponseEntity.badRequest().body("Id do departamento nulo!");
    }

}
