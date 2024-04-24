package poli.csi.projeto_integrador.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "repasse_reitoria")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RepasseReitoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "repassado_em", nullable = false)
    private Timestamp dataTempo;
    @Column(name = "valor", precision = 14, scale = 2, nullable = false)
    private BigDecimal valor;
    @ManyToOne
    @JoinColumn(name = "fk_unidade")
    @JsonManagedReference
    private UnidadeAdministrativa reitoria;

}
