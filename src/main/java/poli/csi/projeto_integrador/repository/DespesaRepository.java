package poli.csi.projeto_integrador.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import poli.csi.projeto_integrador.dto.filter.FiltroDespesa;
import poli.csi.projeto_integrador.model.Despesa;

import java.util.ArrayList;

@Repository
public interface DespesaRepository extends JpaRepository<Despesa, Long>, JpaSpecificationExecutor<Despesa> {

    static Specification<Despesa> despesaSpec(FiltroDespesa filtro) {
        return (despesa, cq,  cb) -> {
            final ArrayList<Predicate> predicates = new ArrayList<Predicate>();

            if( filtro.nome() != null) {
                predicates.add(cb.like(cb.lower(despesa.get("nome")), filtro.nome().toLowerCase().trim() + "%"));
            }

            if( filtro.tipo() != null) {
                predicates.add(cb.like(cb.upper(despesa.get("tipo")), filtro.tipo().toUpperCase().trim() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
