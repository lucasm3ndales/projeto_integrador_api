package poli.csi.projeto_integrador.dto.request;

import java.util.List;

public record ManagersByUnitiesDto(
        List<Long> unityIds
) {
}
