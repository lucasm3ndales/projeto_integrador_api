package poli.csi.projeto_integrador.dto.response;

import lombok.Builder;

@Builder
public record AuthResDto(
        long id,
        String name,
        boolean active,
        String role,
        String token
) {}
