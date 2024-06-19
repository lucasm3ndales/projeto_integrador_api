package poli.csi.projeto_integrador.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import poli.csi.projeto_integrador.model.Budget;

import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    @Query("SELECT b FROM Budget b " +
            "JOIN b.unity ua " +
            "JOIN ua.unityManagers um " +
            "JOIN um.user u " +
            "WHERE u.id = :id " +
            "AND b.year = :year " +
            "ORDER BY b.year DESC " +
            "LIMIT 1")
    Optional<Budget> findLastBudget(@Param("id") Long id, @Param("year") String year);
}
