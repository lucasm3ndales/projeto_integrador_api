package poli.csi.projeto_integrador.repository;

import jakarta.persistence.criteria.Join;
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
import poli.csi.projeto_integrador.model.Tramite;
import poli.csi.projeto_integrador.model.Usuario;

import java.time.LocalDate;
import java.util.ArrayList;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long>, JpaSpecificationExecutor<Evento> {

    @Query("SELECT DISTINCT e FROM Evento e JOIN e.tramites t JOIN t.destino d JOIN t.origem o WHERE d.id = :id OR o.id = :id AND e.arquivado = :arquivado")
    Page<Evento> buscarTodosEventos(@Param("id") Long id, Pageable pageable, @Param("arquivado") Boolean arquivado);

    static Specification<Evento> eventoSpec(Long id, FiltroEvento filtro) {
        return (evento, cq, cb) -> {
            final ArrayList<Predicate> predicates = new ArrayList<Predicate>();

            if(id != null) {
                Join<Evento, Tramite> tramite = evento.join("tramites");
                Join<Tramite, Usuario> usuario = tramite.join("destino");
                predicates.add(cb.equal(usuario.get("id"), id));
            }

            if(filtro.nome() != null) {
                predicates.add(cb.like(cb.lower(evento.get("nome")), filtro.nome().toLowerCase().trim() + "%"));
            }

            if(filtro.tipo() != null) {
                predicates.add(cb.like(cb.upper(evento.get("tipo")), filtro.tipo().toUpperCase().trim() + "%"));
            }

            if(filtro.periodicidade() != null) {
                predicates.add(cb.like(cb.upper(evento.get("periodicidade")), filtro.periodicidade().toUpperCase().trim() + "%"));
            }

            if(filtro.dataInicio() != null && filtro.dataFim() != null) {
                LocalDate dataInicio = LocalDate.parse(filtro.dataFim());
                LocalDate dataFim = LocalDate.parse(filtro.dataInicio());
                predicates.add(cb.between(evento.get("dataInicio"), dataInicio, dataFim));
            }

            if(filtro.status() != null) {
                predicates.add(cb.like(cb.upper(evento.get("status")), filtro.status().toUpperCase().trim() + "%"));
            }

            if(filtro.arquivado() != null) {
                predicates.add(cb.equal(evento.get("arquivado"), filtro.arquivado()));
            }


            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
