package poli.csi.projeto_integrador.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import poli.csi.projeto_integrador.dto.filter.FiltroRepasseReitoria;
import poli.csi.projeto_integrador.model.RepasseReitoria;

import java.time.LocalDate;
import java.util.ArrayList;

@Repository
public interface RepasseReitoriaRepository extends JpaRepository<RepasseReitoria, Long>, JpaSpecificationExecutor<RepasseReitoria> {

    Page<RepasseReitoria> findAllByReitoria_Id(@Param("id") Long id, Pageable pageable);

    static Specification<RepasseReitoria> repasseReitoriaSpec(FiltroRepasseReitoria filtro) {
        return (repasse, cq, cb) -> {
            final ArrayList<Predicate> predicates = new ArrayList<Predicate>();

            if(filtro.dataInicio() != null && filtro.dataFim() != null) {
                LocalDate dataInicio = LocalDate.parse(filtro.dataFim());
                LocalDate dataFim = LocalDate.parse(filtro.dataInicio());
                predicates.add(cb.between(repasse.get("dataInicio"), dataInicio, dataFim));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
