package poli.csi.projeto_integrador.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Timestamp;

@Entity
@Table(name = "tramite")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tramite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "data_tempo", nullable = false)
    private Timestamp dataTempo;
    @Column(name = "status", length = 63, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private StatusTramite status;
    @Column(name = "origem", length = 125, nullable = false)
    private String origem;
    @Column(name = "destino", length = 125, nullable = false)
    private String destino;
    @ManyToOne
    @JoinColumn(name = "fk_evento")
    private Evento evento;

    public enum StatusTramite {EM_TRAMITE, ENCERRADO}
}
