package poli.csi.projeto_integrador.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SaveExpenseDto(
        @NotBlank(message = "Nome inválido!")
        @Size(min = 3, message = "Nome muito curto!")
        String name,
        @NotBlank(message = "Tipo inválido!")
        String type
) {
}
