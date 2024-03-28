package poli.csi.projeto_integrador.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poli.csi.projeto_integrador.dto.request.ReitoriaReqDto;
import poli.csi.projeto_integrador.dto.request.VerbaReqDto;
import poli.csi.projeto_integrador.dto.response.VerbaResDto;
import poli.csi.projeto_integrador.model.Reitoria;
import poli.csi.projeto_integrador.service.ReitoriaService;
import java.util.List;

@RestController(value = "/reitoria")
@AllArgsConstructor
public class ReitoriaController {
    private final ReitoriaService reitoriaService;

    @PostMapping("/salvar")
    public ResponseEntity<String> salvarReitoria(@Valid @RequestBody ReitoriaReqDto dto) {
        boolean res = reitoriaService.salvarReitoria(dto);
        if(res) {
            return ResponseEntity.ok("Reitoria salva com sucesso!");
        }
        return ResponseEntity.internalServerError().body("Erro ao alterar dados da reitoria!");
    }

    @PutMapping("/alterar")
    public ResponseEntity<String> alterarReitoria(@Valid @RequestBody ReitoriaReqDto dto) {
            boolean res = reitoriaService.alterarReitoria(dto);
            if(res) {
                return ResponseEntity.ok("Dados da reitoria alterados com sucesso!");
            }
            return ResponseEntity.internalServerError().body("Erro ao alterar dados da reitoria!");
    }

    @PutMapping("/alterar/status/{id}")
    public ResponseEntity<String> alterarStatusReitoria(@RequestParam("id") Long id) {
        if(id != null) {
            boolean res = reitoriaService.alterarStatusReitoria(id);
            if(res) {
                return ResponseEntity.ok("Status da reitoria alterado com sucesso! ");
            }
            return ResponseEntity.internalServerError().body("Erro ao alterar status da reitoria!");
        }
        return ResponseEntity.badRequest().body("Id da reitoria nulo!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarReitoria(@RequestParam("id") Long id) {
        if(id != null) {
            Reitoria res = reitoriaService.buscarReitoria(id);
            if(res != null) {
                return ResponseEntity.ok(res);
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.badRequest().body("Id da reitoria nulo!");
    }

    @GetMapping("/reitorias")
    public ResponseEntity<List<Reitoria>> buscarReitorias() {
        List<Reitoria> reitorias = reitoriaService.buscarReitorias();
        if(reitorias.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reitorias);
    }

    @PostMapping("/verba-reitoria/adicionar")
    public ResponseEntity<String>  adicionarVerbaReitoria(@Valid @RequestBody VerbaReqDto dto, @RequestHeader("timezone") String timezone) {
            boolean res = reitoriaService.adicionarVerba(dto, timezone);
            if(res) {
                return ResponseEntity.ok("Verba adicionada com sucesso!");
            }
            return ResponseEntity.internalServerError().body("Erro ao adicionar verba a reitoria!");
    }

    @GetMapping("/verba-reitoria/{id}")
    public ResponseEntity<?>  buscarVerbaReitoria(@PathVariable("id") Long id) {
        if(id != null) {
            VerbaResDto res = reitoriaService.buscarVerbaReitoria(id);
            if(res != null) {
                return ResponseEntity.ok(res);
            }
            return ResponseEntity.internalServerError().body("Erro ao buscar dados financeiros!");
        }
        return ResponseEntity.badRequest().body("Id da reitoria nulo!");
    }

}
