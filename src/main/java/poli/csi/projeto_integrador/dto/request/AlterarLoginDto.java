package poli.csi.projeto_integrador.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import poli.csi.projeto_integrador.exception.CustomException;

public record AlterarLoginDto(
        @NotBlank(message = "E-mail de usuário inválido!")
        @Email(message = "E-mail de usuário inválido!")
        String email,
        String login,
        String senha
) {
    public AlterarLoginDto {
        if(login != null && login.length() > 0 && (login.length() < 3 ||  login.isBlank())) {
            throw new CustomException("Nome de usuário curto ou inválido!");
        }

        if(senha != null && senha.length() > 0 && (senha.length() < 6 ||  senha.isBlank())) {
            throw new CustomException("Senha fraca ou inválida!");
        }

        if(senha != null && senha.length() > 0 && senha.equals(login)) {
            throw new CustomException("Senha igual ao nome de usuário!");
        }
    }
}
