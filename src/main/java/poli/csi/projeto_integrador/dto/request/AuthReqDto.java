package poli.csi.projeto_integrador.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AuthReqDto(
        @NotBlank(message = "Nome de usuário inválido!")
        @Size(min = 3)
        String username,
        @NotBlank(message = "Senha inválida!")
        @Size(min = 6)
        String password
)
{}
