package poli.csi.projeto_integrador.dto.request;

import java.math.BigDecimal;

public record ContributionDto(
        Long idUser,
        Long idEvent,
        BigDecimal contribution
) {
}
