package poli.csi.projeto_integrador.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DespesaSalvarDto(
        @NotBlank(message = "Nome inválido!")
        @Size(min = 3, message = "Nome muito curto!")
        String nome,
        @NotBlank(message = "Tipo inválido!")
        String tipo
) {
}
