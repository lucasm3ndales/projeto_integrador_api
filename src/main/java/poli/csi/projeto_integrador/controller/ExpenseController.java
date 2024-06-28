package poli.csi.projeto_integrador.controller;


import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poli.csi.projeto_integrador.dto.filter.FilterExpense;
import poli.csi.projeto_integrador.dto.request.UpdateExpenseDto;
import poli.csi.projeto_integrador.dto.request.SaveExpenseDto;
import poli.csi.projeto_integrador.model.Expense;
import poli.csi.projeto_integrador.service.ExpenseService;

@RestController
@RequestMapping("/expense")
@AllArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;

    @PostMapping("/save")
    public ResponseEntity<String> saveExpense(@RequestBody SaveExpenseDto dto) {
        boolean res = expenseService.save(dto);
        if(res) {
            return ResponseEntity.ok("Despesa cadastrada com sucesso!");
        }
        return ResponseEntity.internalServerError().body("Erro ao cadastrar despesa!");
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateExpense(@RequestBody UpdateExpenseDto dto) {
        boolean res = expenseService.update(dto);
        if(res) {
            return ResponseEntity.ok("Dados da despesa alterados com sucesso!");
        }
        return ResponseEntity.internalServerError().body("Erro ao alterar dados da despesa!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpense(@PathVariable("id") Long id) {
        Expense res = expenseService.getExpense(id);
        if(res != null) {
            return ResponseEntity.ok(res);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/expenses")
    public ResponseEntity<Page<Expense>> getExpenses(
            @PageableDefault(page = 0, size = 200) Pageable pageable,
            @RequestParam(value = "search", required = false) String search
    ) {
        FilterExpense filter = FilterExpense.builder()
                .search(search)
                .build();
        Page<Expense> res = expenseService.getExpenses(pageable, filter);
        if(res != null) {
            return ResponseEntity.ok(res);
        }
        return ResponseEntity.notFound().build();
    }
}
