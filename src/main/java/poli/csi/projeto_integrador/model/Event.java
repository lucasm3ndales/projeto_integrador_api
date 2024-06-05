package poli.csi.projeto_integrador.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "evento")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nome", length = 125, nullable = false)
    private String name;
    @Column(name = "tipo", length = 63, nullable = false)
    @Enumerated(EnumType.STRING)
    private EventType type;
    @Column(name = "periodicidade", length = 63, nullable = false)
    @Enumerated(EnumType.STRING)
    private Periodicity periodicity;
    @Column(name = "data_inicio", nullable = false)
    private LocalDate startDate;
    @Column(name = "data_fim", nullable = false)
    private LocalDate endDate;
    @Column(name = "data_ida", nullable = false)
    private LocalDate departureDate;
    @Column(name = "data_volta", nullable = false)
    private LocalDate backDate;
    @Column(name = "objetivo", length = 500, nullable = false, columnDefinition = "TEXT")
    private String goal;
    @Column(name = "n_participantes", nullable = false)
    private Integer participants;
    @Column(name = "custo", precision = 12, scale = 2, nullable = false)
    private BigDecimal cost;
    @Column(name = "aporte_dep", precision = 12, scale = 2, nullable = false)
    private BigDecimal contributionDep;
    @Column(name = "aporte_reit", precision = 12, scale = 2, nullable = false)
    private BigDecimal contributionReit;
    @Column(name = "arquivado", nullable = false)
    private Boolean archived;
    @Column(name = "status", length = 63, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private EventStatus status;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_endereco")
    private Address address;
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Procedure> procedures = new HashSet<>();
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<EventExpense> eventExpense = new HashSet<>();

    public enum EventType {OUTROS, TECNOLOGIA, SIMPOSIO, CONGRESSO, EXPOFEIRA, FEIRA_LIVRE}

    public enum EventStatus {ACEITO, RECUSADO, PENDENTE}

    public enum Periodicity {ANUALMENTE, SEMESTRALMENTE, TRIMESTRALMENTE, SEMANALMENTE, MENSALMENTE}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event evento = (Event) o;
        return Objects.equals(id, evento.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
