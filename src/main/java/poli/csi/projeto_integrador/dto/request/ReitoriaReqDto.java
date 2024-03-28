package poli.csi.projeto_integrador.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CNPJ;
import poli.csi.projeto_integrador.exception.CustomException;

public record ReitoriaReqDto(
        Long id,
        String name,
        String email,
        String telefone,
        String cnpj,
        String responsavel,
        String login,
        String senha

) {
        public ReitoriaReqDto(
                @NotNull
                @NotBlank
                @Size(min = 2)
                String name,
                @NotNull
                @NotBlank
                @Email
                String email,
                @NotNull
                @NotBlank
                @Size(min = 11)
                String telefone,
                @NotNull
                @NotBlank
                @CNPJ
                String cnpj,
                @NotNull
                @NotBlank
                @Size(min = 2)
                String responsavel,
                @NotNull
                @NotBlank
                @Size(min = 4)
                String login,
                @NotNull
                @NotBlank
                @Size(min = 6)
                String senha
        ) {

                this(null, name, email, telefone, cnpj, responsavel, login, senha);

                if (senha.equals(login)) {
                        throw new CustomException("Senha igual ao nome de usuário!");
                }
        }

        public ReitoriaReqDto(
                @NotNull
                Long id,
                @NotNull
                @NotBlank
                @Size(min = 2)
                String name,
                @NotNull
                @NotBlank
                @Email
                String email,
                @NotNull
                @NotBlank
                @Size(min = 11)
                String telefone,
                @NotNull
                @NotBlank
                @CNPJ
                String cnpj,
                @NotNull
                @NotBlank
                @Size(min = 2)
                String responsavel,
                String login,
                String senha
        ) {
                if(senha != null && (senha.length() < 6 || senha.isBlank())) {
                        throw new CustomException("Senha fraca ou inválida!");
                }

                if (senha != null && senha.equals(login)) {
                        throw new CustomException("Senha igual ao nome de usuário!");
                }

                if(login != null && (login.length() < 4 || login.isBlank())) {
                        throw new CustomException("Nome de usuário inválido!");
                }

                this.id = id;
                this.name = name;
                this.email = email;
                this.telefone = telefone;
                this.cnpj = cnpj;
                this.responsavel = responsavel;
                this.login = login;
                this.senha = senha;
        }
}
