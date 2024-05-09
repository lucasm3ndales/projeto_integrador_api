package poli.csi.projeto_integrador.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateExpenseDto(
        @NotNull(message = "Id da despesa nulo!")
        Long id,
        @NotBlank(message = "Nome inválido!")
        @Size(min = 3, message = "Nome muito curto!")
        String name,
        @NotBlank(message = "Tipo inválido!")
        String type
) {
}
