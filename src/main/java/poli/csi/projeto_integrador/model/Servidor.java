package poli.csi.projeto_integrador.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "servidor")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Servidor extends Usuario {
    @Column(name = "matricula", length = 7, unique = true, nullable = false)
    private String matricula;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<DocServidor> documentos = new HashSet<>();
}
