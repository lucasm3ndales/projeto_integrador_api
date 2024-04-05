package poli.csi.projeto_integrador.dto.request;

import jakarta.validation.constraints.*;
import poli.csi.projeto_integrador.exception.CustomException;

public record AlterarReitoriaDto(
        @NotNull(message = "Id da reitoria nulo!")
        Long id,
        @NotBlank(message = "Nome inválido!")
        @Size(min = 2, message = "Nome muito curto!")
        String nome,
        @NotBlank(message = "E-mail inválido!")
        @Email(message = "E-mail inválido!")
        String email,
        @NotBlank(message = "Telefone inválido!")
        @Size(min = 11, max = 11, message = "Formato de telefone inválido")
        String telefone,
        @NotBlank(message = "Nome do responsável inválido!")
        @Size(min = 2, message = "Nome do responsável muito curto")
        String responsavel
) {}
