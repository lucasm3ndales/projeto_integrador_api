package poli.csi.projeto_integrador.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "despesa_evento")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventExpense {
    @EmbeddedId
    private EventExpenseId id;
    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId("idEvent")
    @JoinColumn(name = "fk_evento", nullable = false)
    @JsonBackReference
    private Event event;
    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId("idExpense")
    @JoinColumn(name = "fk_despesa", nullable = false)
    @JsonManagedReference
    private Expense expense;
    @Column(name = "valor", precision = 12, scale = 2, nullable = false)
    private BigDecimal value;
    @Column(name = "criado_em", nullable = false)
    private Timestamp createdAt;
    @Column(name = "atualizado_em")
    private Timestamp updatedAt;
    @Column(name = "justificativa", length = 500, nullable = false, columnDefinition = "TEXT")
    private String justification;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventExpense that = (EventExpense) o;
        return Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Embeddable
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EventExpenseId implements Serializable {

        @Column(name = "fk_despesa")
        private Long idExpense;

        @Column(name = "fk_evento")
        private Long idEvent;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EventExpenseId that = (EventExpenseId) o;
            return Objects.equals(idEvent, that.idEvent) &&
                    Objects.equals(idExpense, that.idExpense);
        }

        @Override
        public int hashCode() {
            return Objects.hash(idEvent, idExpense);
        }
    }

}
