package poli.csi.projeto_integrador.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private Set<UnityManager> unityManagers;
    @OneToMany()
    @JsonBackReference
    private Set<Procedure> procedures;

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
