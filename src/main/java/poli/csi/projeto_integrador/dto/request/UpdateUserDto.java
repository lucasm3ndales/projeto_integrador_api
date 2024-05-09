package poli.csi.projeto_integrador.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import poli.csi.projeto_integrador.exception.CustomException;

public record UpdateUserDto(
        @NotNull(message = "Id do usuário nulo!")
        Long id,
        @NotBlank(message = "Nome inválido!")
        @Size(min = 2, message = "Nome muito curto!")
        String name,
        @NotBlank(message = "E-mail inválido!")
        @Email(message = "Formato de e-mail inválido!")
        String email,
        @NotBlank(message = "Telefone inválido")
        @Size(max = 11, message = "N° de telefone muito extenso!")
        String phone,
        String username,
        String password
) {
    public UpdateUserDto {
        if(username.compareTo(password) == 0) {
            throw new CustomException("Senha igual ao nome de usuário!");
        }

        if(username != null && username.isBlank()) {
            throw new CustomException("Nome de usuário inválido!");
        }

        if(username != null && username.length() < 3) {
            throw new CustomException("Nome de usuário muito curto!");
        }

        if(password != null && password.isBlank()) {
            throw new CustomException("Senha inválida!");
        }

        if(password != null && password.length() < 6) {
            throw new CustomException("Senha muito curta!");
        }
    }
}
