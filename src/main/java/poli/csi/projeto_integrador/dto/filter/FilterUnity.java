package poli.csi.projeto_integrador.dto.filter;

import lombok.Builder;

@Builder
public record FilterUnity(
        String name,
        String type,
        String manager
) {
}
