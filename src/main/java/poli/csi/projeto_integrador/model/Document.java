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
@Table(name = "documento")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nome", length = 100, nullable = false)
    private String name;
    @Column(name = "tipo", length = 63, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private DocumentType type;
    @Column(name = "doc", nullable = false, columnDefinition = "BYTEA")
    @JsonIgnore
    private byte[] doc;
    @Column(name = "criado_em", nullable = false)
    private Timestamp createdAt;
    @ManyToOne
    @JoinColumn(name = "fk_tramite", nullable = false)
    private Procedure procedure;
    @Column(name = "extensao", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Extensions extension;

    public enum DocumentType {OUTROS}

    public enum Extensions {PDF, DOCX, DOC, TXT, ODT}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Document doc = (Document) o;
        return Objects.equals(id, doc.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}