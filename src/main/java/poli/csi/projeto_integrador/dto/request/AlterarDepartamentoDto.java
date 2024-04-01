package poli.csi.projeto_integrador.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import poli.csi.projeto_integrador.exception.CustomException;

public record AlterarDepartamentoDto(
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
        String responsavel,
        String login,
        String senha
) {
    public AlterarDepartamentoDto{
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
