package poli.csi.projeto_integrador.dto.request;

import java.math.BigDecimal;

public record ContributionDto(
        Long userId,
        Long eventId,
        BigDecimal contribution
) {
}
