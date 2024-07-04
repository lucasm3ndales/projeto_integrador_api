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


@Service
@AllArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;

    public boolean decrementBudget(User user1, Event event, Procedure procedure) {

        String year = String.valueOf(LocalDate.now().getYear());

        Budget budgetUser1 = budgetRepository.findLastBudget(user1.getId(), year)
                .orElseThrow(() -> new EntityNotFoundException("Unidade remetente sem orçamento!"));

        User user2 = procedure.getOrigin();


        if(user2.getRole() == User.UserType.PRO_REITOR || user2.getRole() == User.UserType.CHEFE_DEPARTAMENTO) {
            Budget budgetUser2 = budgetRepository.findLastBudget(user2.getId(), year)
                    .orElseThrow(() -> new EntityNotFoundException("Unidade destinatária sem orçamento!"));

            if(user1.getRole() == User.UserType.CHEFE_DEPARTAMENTO) {

                BigDecimal newBudgetUser1 = budgetUser1.getBudget().subtract(event.getContributionDep());
                BigDecimal newBudgetUser2 = budgetUser2.getBudget().subtract(event.getContributionReit());

                budgetUser1.setBudget(newBudgetUser1);
                budgetUser2.setBudget(newBudgetUser2);

                BigDecimal newBalanaceUser1 = budgetUser1.getBalance().add(event.getContributionDep());
                BigDecimal newBalanaceUser2 = budgetUser2.getBalance().add(event.getContributionReit());

                budgetUser1.setBalance(newBalanaceUser1);
                budgetUser2.setBalance(newBalanaceUser2);

            } else {
                BigDecimal newBudgetUser1 = budgetUser1.getBudget().subtract(event.getContributionReit());
                BigDecimal newBudgetUser2 = budgetUser2.getBudget().subtract(event.getContributionDep());

                budgetUser1.setBudget(newBudgetUser1);
                budgetUser2.setBudget(newBudgetUser2);

                BigDecimal newBalanaceUser1 = budgetUser1.getBalance().add(event.getContributionReit());
                BigDecimal newBalanaceUser2 = budgetUser2.getBalance().add(event.getContributionDep());

                budgetUser1.setBalance(newBalanaceUser1);
                budgetUser2.setBalance(newBalanaceUser2);
            }
            budgetRepository.save(budgetUser2);
        } else {
            BigDecimal newBudgetUser1 = budgetUser1.getBudget().subtract(event.getContributionDep());
            budgetUser1.setBudget(newBudgetUser1);
            BigDecimal newBalanaceUser1 = budgetUser1.getBalance().add(event.getContributionDep());
            budgetUser1.setBalance(newBalanaceUser1);
        }

        budgetRepository.save(budgetUser1);

       return true;
    }

}
