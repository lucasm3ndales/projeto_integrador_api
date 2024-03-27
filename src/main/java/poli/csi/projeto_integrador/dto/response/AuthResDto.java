package poli.csi.projeto_integrador.dto.response;

public record AuthResDto(
        long id,
        String nome,
        boolean status,
        String role,
        String token
) {}
