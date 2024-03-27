package poli.csi.projeto_integrador.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Endereco {
    @Column(name = "pais", length = 100, nullable = false)
    private String pais;
    @Column(name = "estado", length = 8, nullable = false)
    private String estado;
    @Column(name = "cidade", length = 100, nullable = false)
    private String cidade;
    @Column(name = "bairro", length = 100, nullable = false)
    private String bairro;
    @Column(name = "rua", length = 100, nullable = false)
    private String rua;
    @Column(name = "numero", length = 8, nullable = false)
    private String numero;
    @Column(name = "complemento")
    private String complemento;

}
