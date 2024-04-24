package poli.csi.projeto_integrador.controller;


import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poli.csi.projeto_integrador.dto.filtro.DespesaFiltro;
import poli.csi.projeto_integrador.dto.request.DespesaAlterarDto;
import poli.csi.projeto_integrador.dto.request.DespesaSalvarDto;
import poli.csi.projeto_integrador.model.Despesa;
import poli.csi.projeto_integrador.service.DespesaService;

@RestController
@RequestMapping("/despesa")
@AllArgsConstructor
public class DespesaController {
    private final DespesaService despesaService;

    @PostMapping("/salvar")
    public ResponseEntity<String> salvarDespesa(DespesaSalvarDto dto) {
        boolean res = despesaService.salvar(dto);
        if(res) {
            return ResponseEntity.ok("Despesa cadastrada com sucesso!");
        }
        return ResponseEntity.internalServerError().body("Erro ao cadastrar despesa!");
    }

    @PutMapping("/alterar")
    public ResponseEntity<String> alterarDespesa(DespesaAlterarDto dto) {
        boolean res = despesaService.alterar(dto);
        if(res) {
            return ResponseEntity.ok("Dados da despesa alterados com sucesso!");
        }
        return ResponseEntity.internalServerError().body("Erro ao alterar dados da despesa!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Despesa> buscarDespesa(@PathVariable("id") Long id) {
        Despesa res = despesaService.buscarDespesa(id);
        if(res != null) {
            return ResponseEntity.ok(res);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/despesas")
    public ResponseEntity<Page<Despesa>> buscarDespesas(
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "tipo", required = false) String tipo
    ) {
        DespesaFiltro filtro = new DespesaFiltro(nome, tipo);
        Page<Despesa> res = despesaService.buscarDespesas(pageable, filtro);
        if(res != null) {
            return ResponseEntity.ok(res);
        }
        return ResponseEntity.notFound().build();
    }
}
