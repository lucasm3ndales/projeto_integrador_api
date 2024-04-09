package poli.csi.projeto_integrador.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AlterarDespesaDto(
        @NotNull(message = "Id da despesa nulo!")
        Long id,
        @NotBlank(message = "Nome de despesa inválido!")
        String nome,
        @NotBlank(message = "Tipo de despesa inválido!")
        String tipo,
        @NotNull(message = "Valor de despesa inválido")
        @Positive(message = "Valor de despesa negativo!")
        @Min(value = 0, message = "Valor de despesa negativo!")
        BigDecimal valor,
        String justificativa
) {}
