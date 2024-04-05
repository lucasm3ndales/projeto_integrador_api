package poli.csi.projeto_integrador.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import poli.csi.projeto_integrador.dto.filter.FiltroRepasseDepartamento;
import poli.csi.projeto_integrador.model.RepasseDepartamento;

import java.util.ArrayList;

public interface RepasseDepartamentoRepository extends JpaRepository<RepasseDepartamento, Long>, JpaSpecificationExecutor<RepasseDepartamento> {

    Page<RepasseDepartamento> findAllByDepartamento_Id(@Param("id") Long id, Pageable pageable);

    static Specification<RepasseDepartamento> repasseDepartamentoSpec(FiltroRepasseDepartamento filtro) {
        return (repasse, cq, cb) -> {
            final ArrayList<Predicate> predicates = new ArrayList<Predicate>();
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
