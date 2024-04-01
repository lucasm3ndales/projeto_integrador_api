package poli.csi.projeto_integrador.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poli.csi.projeto_integrador.dto.filter.FiltroDepartamento;
import poli.csi.projeto_integrador.dto.request.AlterarDepartamentoDto;
import poli.csi.projeto_integrador.dto.request.DepartamentoVerbaDto;
import poli.csi.projeto_integrador.dto.request.SalvarDepartamentoDto;
import poli.csi.projeto_integrador.dto.response.StatusResDto;
import poli.csi.projeto_integrador.dto.response.VerbaResDto;
import poli.csi.projeto_integrador.model.Departamento;
import poli.csi.projeto_integrador.service.DepartamentoService;

@RestController
@RequestMapping("/departamento")
@AllArgsConstructor
public class DepartamentoController {
    private final DepartamentoService departamentoService;

    @PostMapping("/salvar")
    public ResponseEntity<String> salvarDepartamento(@Valid @RequestBody SalvarDepartamentoDto dto) {
        boolean res = departamentoService.salvarDepartamento(dto);
        if(res) {
            return ResponseEntity.ok("Departamento salvo com sucesso!");
        }
        return ResponseEntity.internalServerError().body("Erro ao salvar departamento!");
    }

    @PutMapping("/alterar")
    public ResponseEntity<String> alterarDepartamento(@Valid @RequestBody AlterarDepartamentoDto dto) {
        boolean res = departamentoService.alterarDepartamento(dto);
        if(res) {
            return ResponseEntity.ok("Dados do departamento alterados com sucesso!");
        }
        return ResponseEntity.internalServerError().body("Erro ao alterar dados do departamento!");
    }

    @PutMapping("/alterar/status/{id}")
    public ResponseEntity<?> alterarStatusDepartamento(@PathVariable("id") Long id) {
        if(id != null) {
            StatusResDto res = departamentoService.alterarStatusDepartamento(id);
            if(res != null) {
                return ResponseEntity.ok(res);
            }
            return ResponseEntity.internalServerError().body("Erro ao alterar status do departamento!");
        }
        return ResponseEntity.badRequest().body("Id do departamento nulo!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarDepartamento(@PathVariable("id") Long id) {
        if(id != null) {
            Departamento res = departamentoService.buscarDepartamento(id);
            if(res != null) {
                return ResponseEntity.ok(res);
            }
            return ResponseEntity.internalServerError().body("Erro ao buscar departamento!");
        }
        return ResponseEntity.badRequest().body("Id do departamento nulo!");
    }

    @GetMapping("/departamentos")
    public ResponseEntity<Page<Departamento>> buscarDepartamentos(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "responsavel", required = false) String responsavel,
            @RequestParam(value = "status", required = false) boolean status
    ) {
            FiltroDepartamento filtro = new FiltroDepartamento(nome, status, responsavel);
            Page<Departamento> res = departamentoService.buscarDepartamentos(pageable, filtro);
            if(res.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(res);
    }

    @PutMapping("/verba-departamento/adicionar")
    public ResponseEntity<String>  adicionarVerbaDepartamento(@Valid @RequestBody DepartamentoVerbaDto dto, @RequestHeader("timezone") String timezone) {
        boolean res = departamentoService.adicionarVerba(dto, timezone);
        if(res) {
            return ResponseEntity.ok("Verba adicionada com sucesso!");
        }
        return ResponseEntity.internalServerError().body("Erro ao adicionar verba ao departamento!");
    }

    @GetMapping("/verba-departamento/{id}")
    public ResponseEntity<?>  buscarVerbaDepartamento(@PathVariable("id") Long id) {
        if(id != null) {
            VerbaResDto res = departamentoService.buscarVerbaDepartamento(id);
            if(res != null) {
                return ResponseEntity.ok(res);
            }
            return ResponseEntity.internalServerError().body("Erro ao buscar dados financeiros!");
        }
        return ResponseEntity.badRequest().body("Id do departamento nulo!");
    }

}
