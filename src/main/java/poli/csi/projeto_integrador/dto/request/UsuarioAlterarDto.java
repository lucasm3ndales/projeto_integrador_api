package poli.csi.projeto_integrador.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import poli.csi.projeto_integrador.exception.CustomException;

public record UsuarioAlterarDto(
        @NotNull(message = "Id do usuário nulo!")
        Long id,
        @NotBlank(message = "Nome inválido!")
        @Size(min = 2, message = "Nome muito curto!")
        String nome,
        @NotBlank(message = "E-mail inválido!")
        @Email(message = "Formato de e-mail inválido!")
        String email,
        @NotBlank(message = "Telefone inválido")
        @Size(max = 11, message = "N° de telefone muito extenso!")
        String telefone,
        String login,
        String senha
) {
    public UsuarioAlterarDto {
        if(login.compareTo(senha) == 0) {
            throw new CustomException("Senha igual ao nome de usuário!");
        }

        if(login != null && login.isBlank()) {
            throw new CustomException("Nome de usuário inválido!");
        }

        if(login != null && login.length() < 3) {
            throw new CustomException("Nome de usuário muito curto!");
        }

        if(senha != null && senha.isBlank()) {
            throw new CustomException("Senha inválida!");
        }

        if(senha != null && senha.length() < 6) {
            throw new CustomException("Senha muito curta!");
        }
    }
}
