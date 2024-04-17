package poli.csi.projeto_integrador.dto.request;

import jakarta.validation.constraints.*;

public record SalvarDespesaDto(
        @NotBlank(message = "Nome de despesa inválido!")
        String nome,
        @NotBlank(message = "Tipo de despesa inválido!")
        String tipo
) {
}
