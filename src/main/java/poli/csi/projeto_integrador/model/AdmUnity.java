package poli.csi.projeto_integrador.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "unidade_administrativa")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    @OneToMany(mappedBy = "unity")
    private Set<UnityManager> unityManagers;

    public enum UnityType {
        REITORIA,
        DEPARTAMENTO
    }
}
