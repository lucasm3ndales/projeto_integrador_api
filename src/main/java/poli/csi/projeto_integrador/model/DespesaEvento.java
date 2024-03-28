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
@Table(name = "despesa_evento")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DespesaEvento {
    @Id
    @ManyToOne
    @JoinColumn(name = "fk_evento", nullable = false)
    private Evento evento;
    @Id
    @ManyToOne
    @JoinColumn(name = "fk_despesa", nullable = false)
    private Despesa despesa;
    @Column(name = "valor", precision = 12, scale = 2, nullable = false)
    private BigDecimal valor;
    @Column(name = "data_tempo", nullable = false)
    private Timestamp dataTempo;
    @Column(name = "justificativa", length = 500, nullable = false, columnDefinition = "TEXT")
    private String justificativa;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DespesaEvento de = (DespesaEvento) o;
        return Objects.equals(evento.getId(), de.evento.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(evento.getId());
    }

}
