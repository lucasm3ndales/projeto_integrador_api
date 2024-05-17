package poli.csi.projeto_integrador.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import poli.csi.projeto_integrador.dto.filter.FilterUnity;
import poli.csi.projeto_integrador.dto.request.SaveUnityDto;
import poli.csi.projeto_integrador.dto.request.UpdateUnityDto;
import poli.csi.projeto_integrador.exception.CustomException;
import poli.csi.projeto_integrador.model.AdmUnity;
import poli.csi.projeto_integrador.model.User;
import poli.csi.projeto_integrador.repository.AdmUnityRepository;
import poli.csi.projeto_integrador.repository.UserRepository;

@Service
@AllArgsConstructor
public class AdmUnityService {
    private final AdmUnityRepository admUnityRepository;
    private final UserRepository userRepository;
    private final UnityManagerService unityManagerService;

    @Transactional
    public boolean save(SaveUnityDto dto, String timezone) {
        AdmUnity.UnityType type = null;

        try{
            type = AdmUnity.UnityType.valueOf(dto.type().trim());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Tipo de unidade administrativa inválido!");
        }

        User user = userRepository.findById(dto.idUser())
                .orElseThrow(() -> new EntityNotFoundException("Servidor responsável pela unidade não encontrado!"));

        AdmUnity unity = AdmUnity.builder()
                .name(dto.name().toLowerCase().trim())
                .type(type)
                .build();

        boolean res = unityManagerService.saveUniteManager(user, unity, timezone);

        if(!res) {
            throw new CustomException("Erro ao registrar servidor responsável pela unidade!");
        }

        admUnityRepository.save(unity);
        return true;
    }

    @Transactional
    public boolean update(UpdateUnityDto dto, String timezone) {
        User user = userRepository.findById(dto.idUser())
                .orElseThrow(() -> new EntityNotFoundException("Servidor responsável pela unidade não encontrado!"));

        AdmUnity unity = admUnityRepository.findById(dto.idUnity())
                .orElseThrow(() -> new EntityNotFoundException("Unidade administrativa não encontrada!"));

        unity.setName(dto.name().toLowerCase().trim());

        boolean res = unityManagerService.saveUniteManager(user, unity, timezone);

        if(!res) {
            throw new CustomException("Erro ao registrar servidor responsável pela unidade!");
        }

        admUnityRepository.save(unity);
        return true;
    }

    public AdmUnity getUnity(Long id) {
        return admUnityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Unidade administrativa não encontrada!"));
    }

    public Page<AdmUnity> getUnities(FilterUnity filter, Pageable pageable) {
       if(isFilter(filter)) {
           Specification<AdmUnity> spec = AdmUnityRepository.specAdmUnity(filter);
           return admUnityRepository.findAll(spec, pageable);
       } else {
           return admUnityRepository.findAll(pageable);
       }
    }

    private boolean isFilter(FilterUnity f) {
        return f.name() != null && !f.name().trim().isEmpty() || f.type() != null && !f.type().trim().isEmpty();
    }


}
