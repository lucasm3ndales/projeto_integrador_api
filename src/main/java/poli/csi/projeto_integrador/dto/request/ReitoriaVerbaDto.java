package poli.csi.projeto_integrador.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ReitoriaVerbaDto(
        @NotNull
        Long id,
        @NotNull
        @Positive
        @Min(1)
        BigDecimal verba
) {}
