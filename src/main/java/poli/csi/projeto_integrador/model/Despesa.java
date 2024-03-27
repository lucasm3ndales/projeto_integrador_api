package poli.csi.projeto_integrador.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

@Entity
@Table(name = "despesa")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Despesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nome", length = 100, nullable = false)
    private String nome;
    @Column(name = "tipo", length = 63, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private TipoDespesa tipo;
    @Column(name = "descricao", length = 500, nullable = false, columnDefinition = "TEXT")
    private String descricao;
    @OneToMany(cascade = CascadeType.ALL)
    private Set<DespesaEvento> despesaEventos;

    public enum TipoDespesa {OUTROS}
}
