package poli.csi.projeto_integrador.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "despesa_evento")
@IdClass(DespesaEventoId.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DespesaEvento {
    @Id
    @ManyToOne
    @JoinColumn(name = "fk_evento", nullable = false)
    @JsonBackReference
    private Evento evento;
    @Id
    @ManyToOne
    @JoinColumn(name = "fk_despesa", nullable = false)
    @JsonManagedReference
    private Despesa despesa;
    @Column(name = "valor", precision = 12, scale = 2, nullable = false)
    private BigDecimal valor;
    @Column(name = "criado_em", nullable = false)
    private Timestamp criadoEm;
    @Column(name = "atualizado_em", nullable = false)
    private Timestamp atualizadoEm;
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
