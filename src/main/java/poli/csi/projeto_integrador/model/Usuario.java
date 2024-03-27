package poli.csi.projeto_integrador.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "usuario")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", length = 125, nullable = false)
    private String nome;
    @Column(name = "email", length = 125, nullable = false)
    private String email;
    @Column(name = "telefone", length = 11, nullable = false)
    private String telefone;
    @Column(name = "status", nullable = false)
    private Boolean status;
    @Column(name = "role", length = 63, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private TipoUsuario role;
    @Column(name = "login", length = 100, nullable = false)
    @JsonIgnore
    private String login;
    @Column(name = "senha", nullable = false)
    @JsonIgnore
    private String senha;

    public enum TipoUsuario {REITORIA, DEPARTAMENTO, SERVIDOR}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
