package poli.csi.projeto_integrador.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "despesa_evento")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DespesaEvento {
    @Id
    @Column(name = "fk_despesa", nullable = false)
    private Long fkDespesa;
    @Id
    @Column(name = "fk_evento", nullable = false)
    private Long fkEvento;
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

}
