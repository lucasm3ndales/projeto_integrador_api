package poli.csi.projeto_integrador.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poli.csi.projeto_integrador.dto.request.ManagersByUnitiesDto;
import poli.csi.projeto_integrador.dto.response.UnityManagerDepDto;
import poli.csi.projeto_integrador.service.UnityManagerService;

import java.util.Set;

@RestController
@AllArgsConstructor
@RequestMapping("/manager")
public class AdmManagerController {
    private final UnityManagerService unityManagerService;


    @PostMapping("/unity")
    public ResponseEntity<Set<UnityManagerDepDto>> getUnityManagersByUnitiesIds(@RequestBody ManagersByUnitiesDto dto) {
        Set<UnityManagerDepDto> managers = unityManagerService.getUnityManagersByUnitiesIds(dto);
        return ResponseEntity.ok(managers);
    }
}
