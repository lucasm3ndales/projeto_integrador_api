package poli.csi.projeto_integrador.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "documento_evento")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DocEvento extends Documento {
    @ManyToOne
    @JoinColumn(name = "fk_evento")
    private Evento evento;
}
