package poli.csi.projeto_integrador.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poli.csi.projeto_integrador.model.Despesa;

@Repository
public interface DespesaRepository extends JpaRepository<Despesa, Long> {
}
