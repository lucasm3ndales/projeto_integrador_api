package poli.csi.projeto_integrador.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import poli.csi.projeto_integrador.exception.CustomException;

import java.math.BigDecimal;

public record DepartamentoVerbaDto(
        @NotNull
        Long reitoriaId,
        @NotNull
        Long departamentoId,
        @NotNull
        @Positive
        @Min(1)
        BigDecimal verba
) {}
