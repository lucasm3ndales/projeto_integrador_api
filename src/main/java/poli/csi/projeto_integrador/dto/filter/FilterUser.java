package poli.csi.projeto_integrador.dto.filter;

public record FilterUser(
        String search,
        String role,
        Boolean active
) {
}
