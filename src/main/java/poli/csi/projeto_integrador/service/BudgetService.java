package poli.csi.projeto_integrador.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import poli.csi.projeto_integrador.model.Budget;
import poli.csi.projeto_integrador.model.Event;
import poli.csi.projeto_integrador.model.Procedure;
import poli.csi.projeto_integrador.model.User;
import poli.csi.projeto_integrador.repository.BudgetRepository;
import java.math.BigDecimal;
import java.time.*;
import java.util.Comparator;


@Service
@AllArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;

    public boolean decrementBudget(User destiny, Event event, Procedure procedure) {

        String year = String.valueOf(LocalDate.now().getYear());

        Budget budgetDestiny = budgetRepository.findLastBudget(destiny.getId(), year)
                .orElseThrow(() -> new EntityNotFoundException("Unidade sem orçamento! 1"));

        System.out.println(budgetDestiny);

        User origin = procedure.getOrigin();

        Budget budgetOrigin = budgetRepository.findLastBudget(origin.getId(), year)
                .orElseThrow(() -> new EntityNotFoundException("Unidade sem orçamento! 2"));

        System.out.println(budgetOrigin);

        if(destiny.getRole() == User.UserType.CHEFE_DEPARTAMENTO) {
            BigDecimal balanceDestiny = budgetDestiny.getBalance().subtract(event.getContributionDep());
            BigDecimal balanceOrigin = budgetOrigin.getBalance().subtract(event.getContributionReit());

            budgetDestiny.setBudget(balanceDestiny);
            budgetOrigin.setBudget(balanceOrigin);

            budgetDestiny.setBudget(event.getContributionDep());
            budgetOrigin.setBudget(event.getContributionReit());

        } else {
            BigDecimal balanceDestiny = budgetDestiny.getBalance().subtract(event.getContributionReit());
            BigDecimal balanceOrigin = budgetOrigin.getBalance().subtract(event.getContributionDep());

            budgetDestiny.setBudget(balanceDestiny);
            budgetOrigin.setBudget(balanceOrigin);

            budgetDestiny.setBudget(event.getContributionReit());
            budgetOrigin.setBudget(event.getContributionDep());
        }

        budgetRepository.save(budgetDestiny);
        budgetRepository.save(budgetOrigin);

       return true;
    }

    private Procedure getLastProcedure(Event event) {
        return event.getProcedures().stream()
                .max(Comparator.comparing(Procedure::getCreatedAt))
                .orElseThrow(() -> new EntityNotFoundException("Não há trâmites ligados ao evento!"));
    }

}
