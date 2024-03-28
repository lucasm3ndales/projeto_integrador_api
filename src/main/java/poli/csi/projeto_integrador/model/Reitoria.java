package poli.csi.projeto_integrador.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "reitoria")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reitoria extends Usuario{
    @Column(name = "cnpj", length = 14, unique = true, nullable = false)
    private String cnpj;
    @Column(name = "responsavel", length = 100, nullable = false)
    private String responsavel;
    @Column(name = "verba", precision = 18, scale = 2, nullable = false)
    @JsonIgnore
    private BigDecimal verba;
    @Column(name = "gasto", precision = 18, scale = 2, nullable = false)
    @JsonIgnore
    private BigDecimal gasto;
    @OneToMany(mappedBy = "reitoria", cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<RepasseReitoria> repassesReitoria = new HashSet<>();
    @OneToMany(mappedBy = "reitoria", cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<RepasseDepartamento> repassesDepartamento = new HashSet<>();
}
