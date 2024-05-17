package poli.csi.projeto_integrador.repository;


import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import poli.csi.projeto_integrador.dto.filter.FilterUnity;
import poli.csi.projeto_integrador.model.AdmUnity;
import poli.csi.projeto_integrador.model.UnityManager;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface AdmUnityRepository extends JpaRepository<AdmUnity, Long>, JpaSpecificationExecutor<AdmUnity> {

    static Specification<AdmUnity> specAdmUnity(FilterUnity filter) {
        return (unity, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.name() != null && !filter.name().isEmpty()) {
                predicates.add(cb.like(cb.lower(unity.get("name")), filter.name().toLowerCase() + "%"));
            }

            if (filter.type() != null && !filter.type().isEmpty()) {
                predicates.add(cb.equal(cb.upper(unity.get("type")), filter.type().toUpperCase()));
            }

            if(filter.manager() != null) {
                Join<AdmUnity, UnityManager> managerJoin = unity.join("user");
                predicates.add(cb.equal(managerJoin.get("name"), filter.manager()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
