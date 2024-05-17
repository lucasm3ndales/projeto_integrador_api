package poli.csi.projeto_integrador.dto.filter;

import lombok.Builder;

@Builder
public record FilterUser(
        String search,
        String role,
        Boolean active
) {
}
