package poli.csi.projeto_integrador.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import poli.csi.projeto_integrador.dto.request.ManagersByUnitiesDto;
import poli.csi.projeto_integrador.dto.response.UnityManagerDepDto;
import poli.csi.projeto_integrador.exception.CustomException;
import poli.csi.projeto_integrador.model.AdmUnity;
import poli.csi.projeto_integrador.model.UnityManager;
import poli.csi.projeto_integrador.model.User;
import poli.csi.projeto_integrador.repository.UnityManagerRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class UnityManagerService {
    private final UnityManagerRepository unityManagerRepository;

    public boolean saveUniteManager(User user, AdmUnity unity, String timezone) {

        var unityManagers = unityManagerRepository.findAll();

        for (var u : unityManagers) {
            if(u.getUser().equals(user)) {
                throw new CustomException("Este servidor já administra uma unidade!");
            }
        }

        UnityManager manager = UnityManager.builder()
                .unity(unity)
                .user(user)
                .startedOn(generateTimestamp(timezone))
                .build();

        unityManagerRepository.save(manager);
        return true;
    }

    public Set<UnityManagerDepDto> getUnityManagersByUnitiesIds(ManagersByUnitiesDto dto) {
       var res = unityManagerRepository.findUnityManagerByUnity(dto.unityIds());

       if(res.isEmpty()) {
           throw new EntityNotFoundException("Responável do departamento não encontrado!");
       }

       Set<UnityManagerDepDto> managers = new HashSet<>();

       res.forEach(i -> {
           UnityManagerDepDto item = UnityManagerDepDto
                   .builder()
                   .id(i.getId())
                   .unityId(i.getUnity().getId())
                   .userId(i.getUser().getId())
                   .manager(i.getUser().getName())
                   .build();
           managers.add(item);
       });

       return managers;
    }

    private Timestamp generateTimestamp(String timezone) {
        Instant instant = Instant.now();
        ZoneId zoneId = ZoneId.of(timezone);
        ZonedDateTime zonedDateTime = instant.atZone(zoneId);
        ZonedDateTime utcDateTime = zonedDateTime.withZoneSameInstant(ZoneOffset.UTC);
        return Timestamp.from(utcDateTime.toInstant());
    }
}
