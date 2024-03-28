package poli.csi.projeto_integrador.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "repasse_reitoria")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RepasseReitoria extends Repasse {
    @ManyToOne
    @JoinColumn(name = "fk_reitoria")
    @JsonManagedReference
    private Reitoria reitoria;

}
