package poli.csi.projeto_integrador.dto.request;

import java.math.BigDecimal;

public record EventExpenseDto(
        Long idExpense,
        String  justification,
        BigDecimal value
) {
}
