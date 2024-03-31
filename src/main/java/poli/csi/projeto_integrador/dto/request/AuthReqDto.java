package poli.csi.projeto_integrador.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AuthReqDto(
        @NotBlank
        @Size(min = 3)
        String login,
        @NotBlank
        @Size(min = 6)
        String senha
)
{}
