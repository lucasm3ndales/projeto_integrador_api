package poli.csi.projeto_integrador.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AlterarDespesaDto(
        @NotNull(message = "Id da despesa nulo!")
        Long id,
        @NotBlank(message = "Nome de despesa inválido!")
        String nome,
        @NotBlank(message = "Tipo de despesa inválido!")
        String tipo
) {}
