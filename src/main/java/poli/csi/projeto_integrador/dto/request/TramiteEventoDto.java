package poli.csi.projeto_integrador.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TramiteEventoDto(
        @NotNull(message = "Id do usuário remetente nulo!")
        Long origemId,
        @NotNull(message = "Id do usuário destinatario nulo!")
        Long destinoId,
        @NotNull(message = "Id do evento nulo!")
        Long eventoId,
        @NotNull(message = "Aporte nulo!")
        @DecimalMin(value = "0", message = "Aporte negativo!")
        BigDecimal aporte
) {}
