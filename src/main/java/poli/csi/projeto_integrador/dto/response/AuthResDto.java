package poli.csi.projeto_integrador.dto.response;

public record AuthResDto(
        long id,
        String name,
        boolean active,
        String role,
        String token
) {}
