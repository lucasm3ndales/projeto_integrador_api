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
@IdClass(DespesaEventoKey.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DespesaEvento {
    @Id
    @Column(name = "fk_despesa", nullable = false)
    private Long idDespesa;
    @Id
    @Column(name = "fk_evento", nullable = false)
    private Long idEvento;
    @Column(name = "valor", precision = 12, scale = 2, nullable = false)
    private BigDecimal valor;
    @Column(name = "data_tempo", nullable = false)
    private Timestamp dataTempo;
    @Column(name = "justificativa", length = 500, nullable = false, columnDefinition = "TEXT")
    private String justificativa;
    @ManyToOne
    @JoinColumn(name = "fk_evento", nullable = false)
    private Evento evento;
    @ManyToOne
    @JoinColumn(name = "fk_despesa", nullable = false)
    private Despesa despesa;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DespesaEvento de = (DespesaEvento) o;
        return Objects.equals(idDespesa, de.idDespesa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idDespesa);
    }

}
