package poli.csi.projeto_integrador.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "gestor_unidade")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UnityManager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_usuario")
    @JsonBackReference
    private User user;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_unidade")
    @JsonManagedReference
    private AdmUnity unity;
    @Column(name = "assumiu_em", nullable = false)
    private Timestamp startedOn;
    @Column(name = "saiu_em")
    private Timestamp leftOn;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnityManager that = (UnityManager) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
