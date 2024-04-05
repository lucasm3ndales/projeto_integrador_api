package poli.csi.projeto_integrador.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Objects;

@Entity
@Table(name = "documento_evento")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentoEvento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nome", length = 100, nullable = false)
    private String nome;
    @Column(name = "tipo", length = 63, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private TipoDocumento tipo;
    @Column(name = "doc", nullable = false, columnDefinition = "BYTEA")
    private byte[] doc;
    @ManyToOne
    @JoinColumn(name = "fk_evento")
    private Evento evento;

    public enum TipoDocumento {OUTROS}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DocumentoEvento doc = (DocumentoEvento) o;
        return Objects.equals(id, doc.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
