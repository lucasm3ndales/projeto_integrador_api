package poli.csi.projeto_integrador.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import poli.csi.projeto_integrador.exception.CustomException;

public record SaveUserDto(
        @NotBlank(message = "Nome inválido!")
        @Size(min = 2, message = "Nome muito curto!")
        String name,
        @NotBlank(message = "E-mail inválido!")
        @Email(message = "Formato de e-mail inválido!")
        String email,
        @NotBlank(message = "Telefone inválido")
        @Size(max = 11, message = "N° de telefone muito extenso!")
        String phone,
        @NotBlank(message = "Matrícula inválida!")
        @Size(min = 7, max = 7, message = "Matrícula deve ter 7 dígitos!")
        String siape,
        @NotBlank(message = "Nome de usuário inválido")
        @Size(min = 4, message = "Nome de usuário muito curto!")
        String username,
        @NotBlank(message = "Senha inválida")
        @Size(min = 6, message = "Senha muito curta!")
        String password

) {
        public SaveUserDto {
                if(username.compareTo(password) == 0) {
                        throw new CustomException("Senha igual ao nome de usuário!");
                }
        }
}
