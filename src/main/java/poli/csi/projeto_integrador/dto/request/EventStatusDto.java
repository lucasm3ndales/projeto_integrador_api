package poli.csi.projeto_integrador.dto.request;

public record EventStatusDto(
        String status,
        Long eventId,
        Long userId
) {}
