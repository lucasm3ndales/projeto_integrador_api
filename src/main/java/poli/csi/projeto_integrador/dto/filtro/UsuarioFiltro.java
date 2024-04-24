package poli.csi.projeto_integrador.dto.filtro;

public record UsuarioFiltro(
        String search,
        String role,
        Boolean ativo
) {
}
