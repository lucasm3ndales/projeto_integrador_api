package poli.csi.projeto_integrador.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DespesaEventoId implements Serializable {
    private Long eventoId;
    private Long despesaId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DespesaEventoId that = (DespesaEventoId) o;
        return Objects.equals(eventoId, that.eventoId) &&
                Objects.equals(despesaId, that.despesaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventoId, despesaId);
    }
}
