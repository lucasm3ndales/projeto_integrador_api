package poli.csi.projeto_integrador.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
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
    @JsonIgnore
    private byte[] doc;
    @Column(name = "criado_em", nullable = false)
    private Timestamp criadoEm;
    @ManyToOne
    @JoinColumn(name = "fk_usuario", nullable = false)
    private Usuario anexadoPor;
    @ManyToOne
    @JoinColumn(name = "fk_evento")
    private Evento evento;
    @Column(name = "extensao", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Extensao extensao;

    public enum TipoDocumento {OUTROS}

    public enum Extensao {PDF, DOCX, DOC, TXT, ODT}

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
