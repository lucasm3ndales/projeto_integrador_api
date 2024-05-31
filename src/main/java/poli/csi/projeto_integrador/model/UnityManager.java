package poli.csi.projeto_integrador.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "gestor_unidade")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UnityManager {
    @EmbeddedId
    private UnityManagerId id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_usuario")
    @MapsId("idUser")
    @JsonBackReference
    private User user;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_unidade")
    @MapsId("idUnity")
    @JsonBackReference
    private AdmUnity unity;
    @Column(name = "assumiu_em", nullable = false)
    private Timestamp startedOn;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnityManager that = (UnityManager) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Embeddable
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UnityManagerId implements Serializable {

        @Column(name = "fk_usuario")
        private Long idUser;

        @Column(name = "fk_unidade")
        private Long idUnity;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UnityManagerId that = (UnityManagerId) o;
            return Objects.equals(idUnity, that.idUnity) &&
                    Objects.equals(idUser, that.idUser);
        }

        @Override
        public int hashCode() {
            return Objects.hash(idUnity, idUser);
        }
    }
}
