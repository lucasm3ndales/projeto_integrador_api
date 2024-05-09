package poli.csi.projeto_integrador.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

@Entity
@Table(name = "unidade_administrativa")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdmUnity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nome", nullable = false)
    private String name;
    @Column(name = "tipo", length = 63, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UnityType type;
    @OneToMany(mappedBy = "unity", cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<Budget> budgets;

    public enum UnityType {
        REITORIA,
        DEPARTAMENTO
    }
}
