package poli.csi.projeto_integrador.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poli.csi.projeto_integrador.model.Reitoria;

@Repository
public interface ReitoriaRepository extends JpaRepository<Reitoria, Long> {

}
