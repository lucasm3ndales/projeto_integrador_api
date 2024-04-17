package poli.csi.projeto_integrador.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poli.csi.projeto_integrador.dto.filter.FiltroDespesa;
import poli.csi.projeto_integrador.dto.request.AlterarDespesaDto;
import poli.csi.projeto_integrador.dto.request.SalvarDespesaDto;
import poli.csi.projeto_integrador.model.Despesa;
import poli.csi.projeto_integrador.service.DespesaService;

@RestController
@RequestMapping("/despesa")
@AllArgsConstructor
public class DespesaController {
    private final DespesaService despesaService;

    @PostMapping("/salvar")
    public ResponseEntity<String> salvarDespesa(@Valid @RequestBody SalvarDespesaDto dto) {
        boolean res = despesaService.salvarDespesa(dto);
        if(res) {
            return ResponseEntity.ok("Despesa salva com sucesso!");
        }
        return ResponseEntity.internalServerError().body("Erro ao salvar despesa!");
    }

    @PutMapping("/alterar")
    public ResponseEntity<String> alterarDespesa(@Valid @RequestBody AlterarDespesaDto dto) {
        boolean res = despesaService.alterarDespesa(dto);
        if(res) {
            return ResponseEntity.ok("Dados da despesa alterados com sucesso!");
        }
        return ResponseEntity.internalServerError().body("Erro ao alterar dados da despesa!");
    }

    @GetMapping("/despesas")
    public ResponseEntity<Page<Despesa>> buscarDespesas(
           @PageableDefault(page = 0, size = 10) Pageable pageable,
           @RequestParam(value = "nome", required = false) String nome,
           @RequestParam(value = "tipo", required = false) String tipo

    ) {
        FiltroDespesa filtro = new FiltroDespesa(nome, tipo);
        Page<Despesa> res = despesaService.buscarDespesas(pageable, filtro);
        if(res != null) {
            return ResponseEntity.ok(res);
        }
        return ResponseEntity.notFound().build();
    }
}
