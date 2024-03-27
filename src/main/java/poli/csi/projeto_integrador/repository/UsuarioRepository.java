package poli.csi.projeto_integrador.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poli.csi.projeto_integrador.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Long, Usuario> {

}
