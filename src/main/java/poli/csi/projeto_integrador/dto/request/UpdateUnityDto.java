package poli.csi.projeto_integrador.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateUnityDto(
        @NotNull(message = "Id da unidade inválido!")
        Long idUnity,
        @NotNull(message = "Id de responsável inválido!")
        Long idUser,
        @NotBlank(message = "Nome da unidade inválido!")
        String name
) {
}
