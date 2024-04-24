package poli.csi.projeto_integrador.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import poli.csi.projeto_integrador.exception.CustomException;

public record UsuarioSalvarDto(
        @NotBlank(message = "Nome inválido!")
        @Size(min = 2, message = "Nome muito curto!")
        String nome,
        @NotBlank(message = "E-mail inválido!")
        @Email(message = "Formato de e-mail inválido!")
        String email,
        @NotBlank(message = "Telefone inválido")
        @Size(max = 11, message = "N° de telefone muito extenso!")
        String telefone,
        @NotBlank(message = "Matrícula inválida!")
        @Size(min = 7, max = 7, message = "Matrícula deve ter 7 dígitos!")
        String matricula,
        @NotBlank(message = "Nome de usuário inválido")
        @Size(min = 4, message = "Nome de usuário muito curto!")
        String login,
        @NotBlank(message = "Senha inválida")
        @Size(min = 6, message = "Senha muito curta!")
        String senha

) {
        public UsuarioSalvarDto {
                if(login.compareTo(senha) == 0) {
                        throw new CustomException("Senha igual ao nome de usuário!");
                }
        }
}
