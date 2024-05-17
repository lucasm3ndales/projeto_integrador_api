package poli.csi.projeto_integrador.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import poli.csi.projeto_integrador.dto.filter.FilterExpense;
import poli.csi.projeto_integrador.model.Expense;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long>, JpaSpecificationExecutor<Expense> {
    Optional<Expense> findExpenseByNameEqualsIgnoreCase(@Param("name") String name);

    static Specification<Expense> specExpense(FilterExpense filter) {
        return (expense, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.name() != null && !filter.name().isEmpty()) {
                predicates.add(cb.like(expense.get("name"), filter.name() + "%"));
            }

            if (filter.type() != null && !filter.type().isEmpty()) {
                predicates.add(cb.equal(cb.upper(expense.get("type")), filter.type().toUpperCase()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
