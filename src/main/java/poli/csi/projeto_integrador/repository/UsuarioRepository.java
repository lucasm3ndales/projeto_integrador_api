package poli.csi.projeto_integrador.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import poli.csi.projeto_integrador.dto.filtro.UsuarioFiltro;
import poli.csi.projeto_integrador.model.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {
    Optional<Usuario> findUsuarioByLogin(@Param("login") String login);
    Optional<Usuario> findUsuarioByEmailOrLogin(@Param("email") String email, @Param("login") String login);

    static Specification<Usuario> specUsuario(UsuarioFiltro filtro) {
        return (usuario, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtro.search() != null && !filtro.search().isEmpty()) {
                predicates.add(cb.or(cb.equal(usuario.get("nome"), filtro.search()), cb.equal(usuario.get("matricula"), filtro.search())));
            }

            if (filtro.role() != null && !filtro.role().isEmpty()) {
                predicates.add(cb.equal(usuario.get("role"), filtro.role()));
            }

            if (filtro.ativo() != null) {
                predicates.add(cb.equal(usuario.get("ativo"), filtro.ativo()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
