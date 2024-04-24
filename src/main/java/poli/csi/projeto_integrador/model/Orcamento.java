package poli.csi.projeto_integrador.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class Orcamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "verba", nullable = false)
    private BigDecimal verba;
    @Column(name = "saldo", nullable = false)
    private BigDecimal saldo;
    @Column(name = "ano", length = 4, nullable = false)
    private String ano;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference
    private UnidadeAdministrativa unidade;
}
