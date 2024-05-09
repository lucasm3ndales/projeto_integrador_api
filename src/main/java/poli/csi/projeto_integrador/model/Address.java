package poli.csi.projeto_integrador.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "endereco")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "pais", length = 100, nullable = false)
    private String country;
    @Column(name = "estado", length = 8, nullable = false)
    private String state;
    @Column(name = "cidade", length = 100, nullable = false)
    private String city;
    @Column(name = "bairro", length = 100, nullable = false)
    private String district;
    @Column(name = "rua", length = 100, nullable = false)
    private String street;
    @Column(name = "numero", length = 8, nullable = false)
    private String num;
    @Column(name = "complemento")
    private String complement;

}
