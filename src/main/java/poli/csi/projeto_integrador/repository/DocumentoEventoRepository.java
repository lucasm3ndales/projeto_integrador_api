package poli.csi.projeto_integrador.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poli.csi.projeto_integrador.model.DocumentoEvento;

import java.util.List;

@Repository
public interface DocumentoEventoRepository extends JpaRepository<DocumentoEvento, Long> {
    List<DocumentoEvento> findAllByIdIn(List<Long> ids);
}
