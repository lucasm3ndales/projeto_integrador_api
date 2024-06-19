package poli.csi.projeto_integrador.repository;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import poli.csi.projeto_integrador.dto.filter.FilterUser;
import poli.csi.projeto_integrador.model.User;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findUserByUsername(@Param("username") String username);
    Optional<User> findUserByEmailOrUsername(@Param("email") String email, @Param("username") String username);
    @Query("SELECT u FROM User u JOIN u.unityManagers um JOIN um.unity un WHERE un.id = :id")
    Optional<User> findByUnityId(@Param("id") Long id);


    static Specification<User> specUser(FilterUser filter) {
        return (user, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.search() != null && !filter.search().isEmpty()) {
                predicates.add(cb.or(cb.like(user.get("name"), filter.search() + "%"), cb.like(user.get("siape"), filter.search() + "%")));
            }

            if (filter.role() != null && !filter.role().isEmpty()) {
                predicates.add(cb.equal(cb.upper(user.get("role")), filter.role().toUpperCase()));
            }

            if (filter.active() != null) {
                predicates.add(cb.equal(user.get("active"), filter.active()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
