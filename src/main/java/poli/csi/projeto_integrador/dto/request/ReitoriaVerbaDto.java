package poli.csi.projeto_integrador.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ReitoriaVerbaDto(
        @NotNull
        Long id,
        @NotNull
        @DecimalMin(value = "0")
        BigDecimal verba
) {}
