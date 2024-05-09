package poli.csi.projeto_integrador.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "despesa")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nome", length = 100, nullable = false, unique = true)
    private String name;
    @Column(name = "tipo", length = 63, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ExpenseType type;
    @OneToMany(cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<EventExpense> eventExpense = new HashSet<>();

    public enum ExpenseType {OUTROS}
}
