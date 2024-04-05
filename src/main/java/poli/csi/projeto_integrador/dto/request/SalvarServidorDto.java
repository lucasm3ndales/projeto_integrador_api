package poli.csi.projeto_integrador.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import poli.csi.projeto_integrador.exception.CustomException;

public record SalvarServidorDto(
        @NotBlank(message = "Nome inválido!")
        @Size(min = 2, message = "Nome muito curto!")
        String nome,
        @NotBlank(message = "E-mail inválido!")
        @Email(message = "E-mail inválido!")
        String email,
        @NotBlank(message = "Telefone inválido!")
        @Size(min = 11, max = 11, message = "Formato de telefone inválido")
        String telefone,
        @NotBlank(message = "Matrícula inválida!")
        String matricula,
        @NotBlank(message = "Nome de usuário inválido!")
        @Size(min = 3, message = "Nome de usuário muito curto!")
        String login,
        @NotBlank(message = "Senha inválida!")
        @Size(min = 6, message = "Senha fraca!")
        String senha
) {
    public SalvarServidorDto {
        if(senha.equals(login)) {
            throw new CustomException("Senha igual ao nome de usuário!");
        }

        if(matricula.length() != 7) {
            throw new CustomException("Matrícula deve ter 7 dígitos!");
        }
    }
}
