package poli.csi.projeto_integrador.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import poli.csi.projeto_integrador.dto.filtro.DespesaFiltro;
import poli.csi.projeto_integrador.model.Despesa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface DespesaRepository extends JpaRepository<Despesa, Long>, JpaSpecificationExecutor<Despesa> {
    Optional<Despesa> findDespesaByNomeEqualsIgnoreCase(@Param("nome") String nome);

    static Specification<Despesa> specDespesa(DespesaFiltro filtro) {
        return (despesa, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtro.nome() != null && !filtro.nome().isEmpty()) {
                predicates.add(cb.equal(despesa.get("nome"), filtro.nome()));
            }

            if (filtro.tipo() != null && !filtro.tipo().isEmpty()) {
                predicates.add(cb.equal(despesa.get("tipo"), filtro.tipo()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
