package poli.csi.projeto_integrador.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tramite")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Procedure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "tramitado_em", nullable = false)
    private Timestamp createdAt;
    @ManyToOne
    @JoinColumn(name = "fk_origem", nullable = false)
    @JsonManagedReference
    private User origin;
    @ManyToOne
    @JoinColumn(name = "fk_destino", nullable = false)
    @JsonManagedReference
    private User destiny;
    @ManyToOne
    @JoinColumn(name = "fk_evento")
    @JsonBackReference
    private Event event;
    @OneToMany(mappedBy = "procedure", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Document> documents = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Procedure tramite = (Procedure) o;
        return Objects.equals(id, tramite.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
