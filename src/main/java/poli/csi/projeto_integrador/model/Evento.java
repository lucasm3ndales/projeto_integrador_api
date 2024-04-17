package poli.csi.projeto_integrador.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "evento")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nome", length = 125, nullable = false)
    private String nome;
    @Column(name = "tipo", length = 63, nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoEvento tipo;
    @Column(name = "periodicidade", length = 63, nullable = false)
    @Enumerated(EnumType.STRING)
    private Periodicidade periodicidade;
    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;
    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;
    @Column(name = "data_ida", nullable = false)
    private LocalDate dataIda;
    @Column(name = "data_volta", nullable = false)
    private LocalDate dataVolta;
    @Column(name = "objetivo", length = 500, nullable = false, columnDefinition = "TEXT")
    private String objetivo;
    @Column(name = "participantes", nullable = false)
    private Integer participantes;
    @Column(name = "custo", precision = 12, scale = 2, nullable = false)
    private BigDecimal custo;
    @Column(name = "aporte_dep", precision = 12, scale = 2, nullable = false)
    private BigDecimal aporteDep;
    @Column(name = "aporte_reit", precision = 12, scale = 2, nullable = false)
    private BigDecimal aporteReit;
    @Column(name = "arquivado", nullable = false)
    private Boolean arquivado;
    @Column(name = "status", length = 63, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private StatusEvento status;
    @Embedded
    private Endereco endereco;
    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL)
    private Set<DocumentoEvento> documentos = new HashSet<>();
    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL)
    private Set<Tramite> tramites = new HashSet<>();
    @OneToMany(mappedBy = "evento", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<DespesaEvento> despesaEventos = new HashSet<>();

    public enum TipoEvento {OUTROS}

    public enum StatusEvento {ACEITO, RECUSADO, PENDENTE}

    public enum Periodicidade {ANUALMENTE, SEMESTRALMENTE, TRIMESTRALMENTE, SEMANALMENTE}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Evento evento = (Evento) o;
        return Objects.equals(id, evento.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
