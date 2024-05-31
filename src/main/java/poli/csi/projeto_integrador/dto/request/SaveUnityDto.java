package poli.csi.projeto_integrador.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SaveUnityDto(
        @NotBlank(message = "Nome da unidade inválido!")
        String name,
        @NotBlank(message = "Tipo da unidade inválido!")
        String type,
        @NotNull(message = "Id de responsável inválido!")
        Long idUser
) {
}
