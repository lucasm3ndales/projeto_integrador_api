package poli.csi.projeto_integrador.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import poli.csi.projeto_integrador.dto.filter.FilterExpense;
import poli.csi.projeto_integrador.dto.request.EventExpenseDto;
import poli.csi.projeto_integrador.dto.request.UpdateExpenseDto;
import poli.csi.projeto_integrador.dto.request.SaveExpenseDto;
import poli.csi.projeto_integrador.exception.CustomException;
import poli.csi.projeto_integrador.model.Event;
import poli.csi.projeto_integrador.model.EventExpense;
import poli.csi.projeto_integrador.model.Expense;
import poli.csi.projeto_integrador.repository.ExpenseRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;

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

        Expense expense = Expense.builder()
                .name(dto.name().trim())
                .type(type)
                .build();

        expenseRepository.save(expense);
        return true;
    }

    @Transactional
    public boolean update(UpdateExpenseDto dto) {
        Expense isExist = expenseRepository.findExpenseByNameEqualsIgnoreCase(dto.name().trim())
                .orElse(null);

        if (isExist != null && !isExist.getName().equals(dto.name().trim())) {
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
        return f.search() != null || !f.search().isEmpty();
    }


    @Transactional
    public boolean addExpensesToEvent(Event event, List<EventExpenseDto> eventExpenses, String timezone) {
        eventExpenses.forEach(i -> {
            Expense expense = expenseRepository.findById(i.idExpense()).orElseThrow(() -> new EntityNotFoundException("Despesa não encontrada!"));

            EventExpense eventExpense = EventExpense.builder()
                    .event(event)
                    .expense(expense)
                    .createdAt(generateTimestamp(timezone))
                    .value(i.value())
                    .justification(i.justification().trim())
                    .build();

            event.getEventExpenses().add(eventExpense);
            expenseRepository.save(expense);

        });
        return true;
    }

    private Timestamp generateTimestamp(String timezone) {
        Instant instant = Instant.now();
        ZoneId zoneId = ZoneId.of(timezone);
        ZonedDateTime zonedDateTime = instant.atZone(zoneId);
        ZonedDateTime utcDateTime = zonedDateTime.withZoneSameInstant(ZoneOffset.UTC);
        return Timestamp.from(utcDateTime.toInstant());
    }
}
