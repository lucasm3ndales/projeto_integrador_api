package poli.csi.projeto_integrador.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import poli.csi.projeto_integrador.dto.filter.FiltroServidor;
import poli.csi.projeto_integrador.model.Servidor;

import java.util.ArrayList;

@Repository
public interface ServidorRepository extends JpaRepository<Servidor, Long>, JpaSpecificationExecutor<Servidor> {

    //TODO: Implementar filtros para servidor
    static Specification<Servidor> servidorSpec(FiltroServidor filtro) {
        return (servidor, cq,  cb) -> {
            final ArrayList<Predicate> predicates = new ArrayList<Predicate>();
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
