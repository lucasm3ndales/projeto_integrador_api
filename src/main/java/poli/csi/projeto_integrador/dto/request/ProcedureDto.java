package poli.csi.projeto_integrador.dto.request;

import jakarta.validation.constraints.NotNull;

public record ProcedureDto(
        @NotNull(message = "Id do usuário origem inválido!")
        Long originId,
        @NotNull(message = "Id do usuário destino inválido!")
        Long destinyId,
        @NotNull(message = "Id do evento inválido!")
        Long eventId
) {}
