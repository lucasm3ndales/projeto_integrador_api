package poli.csi.projeto_integrador.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import poli.csi.projeto_integrador.dto.filter.FilterExpense;
import poli.csi.projeto_integrador.dto.request.UpdateExpenseDto;
import poli.csi.projeto_integrador.dto.request.SaveExpenseDto;
import poli.csi.projeto_integrador.exception.CustomException;
import poli.csi.projeto_integrador.model.Expense;
import poli.csi.projeto_integrador.repository.ExpenseRepository;

@Service
@AllArgsConstructor
public class ExpenseService {
    private final ExpenseRepository expenseRepository;

    @Transactional
    public boolean save(SaveExpenseDto dto) {
        Expense isExist = expenseRepository.findExpenseByNameEqualsIgnoreCase(dto.name().trim())
                .orElse(null);

        if (isExist != null) {
            throw new CustomException("Despesa já cadastrada no sistema!");
        }

        Expense.ExpenseType type = null;
        try {
            type = Expense.ExpenseType.valueOf(dto.type().trim());
        } catch (IllegalArgumentException ex) {
            throw ex;
        }

        Expense expense = new Expense();
        expense.setName(dto.name().toLowerCase().trim());
        expense.setType(type);

        expenseRepository.save(expense);
        return true;
    }

    @Transactional
    public boolean update(UpdateExpenseDto dto) {
        Expense isExist = expenseRepository.findExpenseByNameEqualsIgnoreCase(dto.name().trim())
                .orElse(null);

        if (isExist != null) {
            throw new CustomException("Despesa já cadastrada no sistema!");
        }

        Expense.ExpenseType type = null;
        try {
            type = Expense.ExpenseType.valueOf(dto.type().trim());
        } catch (IllegalArgumentException ex) {
            throw ex;
        }

        Expense expense = expenseRepository.findById(dto.id())
                .orElseThrow(() -> new EntityNotFoundException("Despesa não encontrada!"));

        expense.setName(dto.name().toLowerCase().trim());
        expense.setType(type);

        expenseRepository.save(expense);
        return true;
    }

    public Expense getExpense(Long id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Despesa não encontrada!"));
    }

    public Page<Expense> getExpenses(Pageable pageable, FilterExpense filter) {
        if(isFilter(filter)) {
            Specification<Expense> spec = ExpenseRepository.specExpense(filter);
            return expenseRepository.findAll(spec, pageable);
        }
        return expenseRepository.findAll(pageable);
    }

    private boolean isFilter(FilterExpense f) {
        return f.name() == null || f.name().trim().isEmpty() ||
                f.type() == null || f.type().trim().isEmpty();
    }
}
