package poli.csi.projeto_integrador.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import poli.csi.projeto_integrador.exception.CustomException;

import java.math.BigDecimal;

public record VerbaReqDto(
   Long reitoriaId,
   Long departamentoId,
   BigDecimal verbaReitoria,
   BigDecimal verbaDepartamento
) {
    public VerbaReqDto(
            @NotNull
            Long reitoriaId,
            @NotNull
            @Positive
            BigDecimal verbaReitoria
    ) {
        this(reitoriaId, null, verbaReitoria, null);

        if(reitoriaId == null) {
            throw new CustomException("Id da reitoria nulo!");
        }

        if(verbaReitoria.compareTo(BigDecimal.ZERO) == -1) {
            throw new CustomException("Valor de repasse negativo!");
        }
    }

    public VerbaReqDto(
            @NotNull
            Long reitoriaId,
            @NotNull
            Long departamentoId,
            @NotNull
            @Positive
            BigDecimal verbaDepartamento

    ) {
        this(reitoriaId, departamentoId, null, verbaDepartamento);

        if(reitoriaId == null) {
            throw new CustomException("Id da reitoria nulo!");
        }

        if(departamentoId == null) {
            throw new CustomException("Id do departamento nulo!");
        }

        if(verbaDepartamento.compareTo(BigDecimal.ZERO) == -1) {
            throw new CustomException("Valor de repasse negativo!");
        }
    }
}
