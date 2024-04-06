package poli.csi.projeto_integrador.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DepartamentoVerbaDto(
        @NotNull
        Long reitoriaId,
        @NotNull
        Long departamentoId,
        @NotNull
        @Positive
        @DecimalMin(value = "0")
        BigDecimal verba
) {}
