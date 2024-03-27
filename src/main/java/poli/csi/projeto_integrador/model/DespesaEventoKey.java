package poli.csi.projeto_integrador.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DespesaEventoKey implements Serializable {
    private Long idEvento;
    private Long idDespesa;
}
