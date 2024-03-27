package poli.csi.projeto_integrador.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "repasse")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Repasse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "data_tempo", nullable = false)
    private Timestamp dataTempo;
    @Column(name = "valor", precision = 18, scale = 2, nullable = false)
    private BigDecimal valor;
    @ManyToOne
    @JoinColumn(name = "fk_reitoria")
    @JsonManagedReference
    private Reitoria reitoria;
    @ManyToOne
    @JoinColumn(name = "fk_departamento")
    @JsonManagedReference
    private Departamento departamento;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Repasse repasse = (Repasse) o;
        return Objects.equals(id, repasse.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
