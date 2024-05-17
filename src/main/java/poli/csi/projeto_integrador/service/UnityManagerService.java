package poli.csi.projeto_integrador.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import poli.csi.projeto_integrador.model.AdmUnity;
import poli.csi.projeto_integrador.model.UnityManager;
import poli.csi.projeto_integrador.model.User;
import poli.csi.projeto_integrador.repository.UnityManagerRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Service
@AllArgsConstructor
public class UnityManagerService {
    UnityManagerRepository unityManagerRepository;

    public boolean saveUniteManager(User user, AdmUnity unity, String timezone) {
        UnityManager manager = UnityManager.builder()
                .unity(unity)
                .user(user)
                .startedOn(generateTimestamp(timezone))
                .build();

        unityManagerRepository.save(manager);
        return true;
    }

    private Timestamp generateTimestamp(String timezone) {
        Instant instant = Instant.now();
        ZoneId zoneId = ZoneId.of(timezone);
        ZonedDateTime zonedDateTime = instant.atZone(zoneId);
        ZonedDateTime utcDateTime = zonedDateTime.withZoneSameInstant(ZoneOffset.UTC);
        return Timestamp.from(utcDateTime.toInstant());
    }
}
