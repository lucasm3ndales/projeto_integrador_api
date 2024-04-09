package poli.csi.projeto_integrador.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import poli.csi.projeto_integrador.dto.filter.FiltroEvento;
import poli.csi.projeto_integrador.model.Evento;

import java.util.ArrayList;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long>, JpaSpecificationExecutor<Evento> {

    @Query("SELECT DISTINCT e FROM Evento e JOIN e.tramites t JOIN t.destino d JOIN t.origem o WHERE d.id = :id OR o.id = :id")
    Page<Evento> buscarTodosEventos(@Param("id") Long id, Pageable pageable);

    //TODO: implementar filtros para evento
    static Specification<Evento> eventoSpec(Long id, FiltroEvento filtro) {
        return (evento, cq, cb) -> {
            final ArrayList<Predicate> predicates = new ArrayList<Predicate>();
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
