package poli.csi.projeto_integrador.dto.response;

import lombok.Builder;

@Builder
public record UnityManagerDepDto(
        Long id,
        Long userId,
        Long unityId,
        String manager
) {
}
