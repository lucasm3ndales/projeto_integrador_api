package poli.csi.projeto_integrador.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nome", length = 125, nullable = false)
    private String name;
    @Column(name = "email", length = 125, nullable = false, unique = true)
    private String email;
    @Column(name = "telefone", length = 11, nullable = false)
    private String phone;
    @Column(name = "ativo", nullable = false)
    private Boolean active;
    @Column(name = "matricula", length = 7, nullable = false, unique = true)
    private String siape;
    @Column(name = "role", length = 63, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserType role;
    @Column(name = "login", length = 100, nullable = false, unique = true)
    @JsonIgnore
    private String username;
    @Column(name = "senha", nullable = false)
    @JsonIgnore
    private String password;

    public enum UserType {PRO_REITOR, CHEFE_DEPARTAMENTO, SERVIDOR}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User usuario = (User) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
