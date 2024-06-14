package poli.csi.projeto_integrador.repository;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import poli.csi.projeto_integrador.dto.filter.FilterEvent;
import poli.csi.projeto_integrador.model.Event;
import poli.csi.projeto_integrador.model.Procedure;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Query("SELECT DISTINCT e FROM Event e JOIN e.procedures p " +
            "JOIN p.origin o " +
            "JOIN p.destiny d " +
            "WHERE (o.id = :id OR d.id = :id) " +
            "AND e.archived = :archived")
    Page<Event> findEventsByUser(Long id, Boolean archived, Pageable pageable);

    static Specification<Event> specEvent(FilterEvent filter, Long idUser) {
        return (event, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(idUser != null) {
                Join<Event, Procedure> procedureJoin = event.join("procedures");
                Predicate originPredicate = cb.equal(procedureJoin.get("origin").get("id"), idUser);
                Predicate destinyPredicate = cb.equal(procedureJoin.get("destiny").get("id"), idUser);
                predicates.add(cb.or(originPredicate, destinyPredicate));
            }

            if (filter.name() != null && !filter.name().isEmpty()) {
                predicates.add(cb.like(cb.lower(event.get("name")), filter.name().toLowerCase() + "%"));
            }

            if (filter.type() != null && !filter.type().isEmpty()) {
                predicates.add(cb.equal(cb.upper(event.get("type")), filter.type().toUpperCase()));
            }

            if (filter.periodicity() != null && !filter.periodicity().isEmpty()) {
                predicates.add(cb.equal(cb.upper(event.get("periodicity")), filter.periodicity().toUpperCase()));
            }

            if (filter.status() != null && !filter.status().isEmpty()) {
                predicates.add(cb.equal(cb.upper(event.get("status")), filter.status().toUpperCase()));
            }

            if (filter.startDate() != null && !filter.startDate().isEmpty()
                    && filter.endDate() != null && !filter.endDate().isEmpty()
            ) {
                predicates.add(cb.greaterThanOrEqualTo(event.get("startDate"), LocalDate.parse(filter.startDate(), formatter)));
                predicates.add(cb.lessThanOrEqualTo(event.get("endDate"), LocalDate.parse(filter.endDate(), formatter)));
            }

            cq.distinct(true);

            predicates.add(cb.equal(event.get("archived"), filter.archived()));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
