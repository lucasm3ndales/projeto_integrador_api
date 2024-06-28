package poli.csi.projeto_integrador.dto.filter;

import lombok.Builder;

@Builder
public record FilterExpense(
        String search
) {
}
