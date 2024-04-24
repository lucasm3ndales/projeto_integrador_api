package poli.csi.projeto_integrador.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "gestor_unidade")
@IdClass(UnidadeUsuarioId.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GestorUnidade {
    @Id
    private Long idUsuario;
    @Id
    private Long idUnidade;
    @Column(name = "assumiu_em", nullable = false)
    private Timestamp assumiuEm;
}
