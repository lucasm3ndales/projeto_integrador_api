package poli.csi.projeto_integrador.dto.response;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record VerbaResDto(
        BigDecimal verba,
        BigDecimal gasto
) {}
