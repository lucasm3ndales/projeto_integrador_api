package poli.csi.projeto_integrador.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import poli.csi.projeto_integrador.dto.filter.FiltroDepartamento;
import poli.csi.projeto_integrador.model.Departamento;

import java.util.ArrayList;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long>, JpaSpecificationExecutor<Departamento> {

    static Specification<Departamento> departamentoSpec(FiltroDepartamento filtro) {
        return (departamento, cq,  cb) -> {
            final ArrayList<Predicate> predicates = new ArrayList<Predicate>();
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
