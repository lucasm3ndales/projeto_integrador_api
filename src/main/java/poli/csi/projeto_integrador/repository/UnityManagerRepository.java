package poli.csi.projeto_integrador.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import poli.csi.projeto_integrador.model.UnityManager;
import java.util.List;
import java.util.Set;

@Repository
public interface UnityManagerRepository extends JpaRepository<UnityManager, Long> {

    @Query("SELECT DISTINCT um FROM UnityManager um JOIN um.unity u " +
            "WHERE u.id IN :ids " +
            "AND um.leftOn is NULL " +
            "AND um.startedOn = (SELECT MAX(um2.startedOn) FROM UnityManager um2 WHERE um2.unity.id = um.unity.id AND um2.leftOn IS NULL)")
    Set<UnityManager> findUnityManagerByUnity(@Param("ids") List<Long> ids);
}
