package poli.csi.projeto_integrador.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AuthReqDto(
        @NotNull
        @NotBlank
        @Size(min = 3, max = 100)
        String login,
        @NotNull
        @NotBlank
        String senha
)
{}
