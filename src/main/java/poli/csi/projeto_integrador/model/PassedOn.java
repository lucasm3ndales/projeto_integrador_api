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
@Table(name = "repasse")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PassedOn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "repassado_em", nullable = false)
    private Timestamp timestamp;
    @Column(name = "valor", precision = 14, scale = 2, nullable = false)
    private BigDecimal value;
    @ManyToOne
    @JoinColumn(name = "fk_origem")
    @JsonManagedReference
    private AdmUnity origin;
    @ManyToOne
    @JoinColumn(name = "fk_destino")
    @JsonManagedReference
    private AdmUnity destiny;
}
