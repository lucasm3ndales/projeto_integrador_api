package poli.csi.projeto_integrador.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class Repasse {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "repasse_seq")
    @SequenceGenerator(name = "repasse_seq", sequenceName = "repasse_seq", allocationSize = 1)
    private Long id;
    @Column(name = "data_tempo", nullable = false)
    private Timestamp dataTempo;
    @Column(name = "valor", precision = 18, scale = 2, nullable = false)
    private BigDecimal valor;

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
