package poli.csi.projeto_integrador.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "tramite")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tramite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "criado_em", nullable = false)
    private Timestamp criadoEm;
    @Column(name = "status", length = 63, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private StatusTramite status;
    @ManyToOne
    @JoinColumn(name = "fk_origem", nullable = false)
    private Usuario origem;
    @ManyToOne
    @JoinColumn(name = "fk_destino", nullable = false)
    private Usuario destino;
    @ManyToOne
    @JoinColumn(name = "fk_evento")
    private Evento evento;

    public enum StatusTramite {EM_ABERTO, ENCERRADO}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tramite tramite = (Tramite) o;
        return Objects.equals(id, tramite.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
