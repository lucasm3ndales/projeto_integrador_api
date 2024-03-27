package poli.csi.projeto_integrador.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "documento")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class Documento {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name = "nome", length = 100, nullable = false)
    private String nome;
    @Column(name = "tipo", length = 63, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private TipoDocumento tipo;
    @Column(name = "doc", nullable = false, columnDefinition = "BYTEA")
    private byte[] doc;

    public enum TipoDocumento {OUTROS}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Documento doc = (Documento) o;
        return Objects.equals(id, doc.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
