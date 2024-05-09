package poli.csi.projeto_integrador.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "orcamento")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "verba", nullable = false)
    private BigDecimal budget;
    @Column(name = "saldo", nullable = false)
    private BigDecimal balance;
    @Column(name = "ano", length = 4, nullable = false)
    private String year;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference
    private AdmUnity unity;
}
