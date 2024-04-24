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
public class UnidadeUsuarioId implements Serializable {
    private Long unidadeId;
    private Long usuarioId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnidadeUsuarioId that = (UnidadeUsuarioId) o;
        return Objects.equals(unidadeId, that.unidadeId) &&
                Objects.equals(usuarioId, that.usuarioId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(unidadeId, usuarioId);
    }
}
