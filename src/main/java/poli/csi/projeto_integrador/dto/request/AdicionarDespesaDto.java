package poli.csi.projeto_integrador.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AdicionarDespesaDto(
        @NotNull(message = "Id da despesa nulo!")
        Long id,
        @NotNull(message = "Valor de despesa inválido")
        @Positive(message = "Valor de despesa negativo!")
        @Min(value = 0, message = "Valor de despesa negativo!")
        BigDecimal valor,
        String justificativa
) {
}
