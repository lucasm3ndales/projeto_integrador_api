package poli.csi.projeto_integrador.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record AlterarStatusEventoDto(
        @NotNull(message = "Id usuário remetente nulo!")
        Long origemId,
        @NotNull(message = "Id de evento nulo!")
        Long eventoId,
        @NotBlank(message = "Status do evento inválido!")
        String status,
        @NotNull(message = "Aporte nulo!")
        @DecimalMin(value = "0", message = "Aporte negativo!")
        BigDecimal aporte
) {}
