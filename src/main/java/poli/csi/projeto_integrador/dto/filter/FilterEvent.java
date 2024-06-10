package poli.csi.projeto_integrador.dto.filter;

import lombok.Builder;

@Builder
public record FilterEvent(
        String name,
        String periodicity,
        String type,
        String status,
        String startDate,
        String endDate,
        Boolean archived
) {
}
