package poli.csi.projeto_integrador.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poli.csi.projeto_integrador.dto.filter.FilterUnity;
import poli.csi.projeto_integrador.dto.request.SaveUnityDto;
import poli.csi.projeto_integrador.dto.request.UpdateUnityDto;
import poli.csi.projeto_integrador.model.AdmUnity;
import poli.csi.projeto_integrador.service.AdmUnityService;

@RestController
@AllArgsConstructor
@RequestMapping("/unity")
public class AdmUnityController {
    private final AdmUnityService admUnityService;

    @PostMapping("/save")
    public ResponseEntity<String> saveUnity(@Valid @RequestBody SaveUnityDto dto, @RequestHeader("timezone") String timezone) {
        boolean res = admUnityService.save(dto, timezone);
        if (res) {
            return ResponseEntity.ok("Unidade administrativa salva com sucesso!");
        }
        return ResponseEntity.internalServerError().body("Erro ao salvar unidade administrativa!");
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUnity(@Valid @RequestBody UpdateUnityDto dto, @RequestHeader("timezone") String timezone) {
        boolean res = admUnityService.update(dto, timezone);
        if (res) {
            return ResponseEntity.ok("Dados da unidade administrativa alterados com sucesso!");
        }
        return ResponseEntity.internalServerError().body("Erro ao alterar dados da unidade administrativa!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdmUnity> getUnity(@PathVariable("id") Long id) {
        AdmUnity unity = admUnityService.getUnity(id);
        if (unity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(unity);
    }

    @GetMapping("/unities")
    public ResponseEntity<Page<AdmUnity>> getUnities(
            @PageableDefault(page = 0, size = 200) Pageable pageable,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "type") String type,
            @RequestParam(value = "manager", required = false) String manager

    ) {
        FilterUnity filter = FilterUnity.builder()
                .name(name)
                .type(type)
                .manager(manager)
                .build();

        Page<AdmUnity> unities = admUnityService.getUnities(filter, pageable);

        if(unities.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(unities);
    }

}
