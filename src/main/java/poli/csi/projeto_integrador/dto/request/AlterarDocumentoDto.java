package poli.csi.projeto_integrador.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AlterarDocumentoDto(
        @NotNull(message = "Id do documento nulo!")
        Long id,
        @NotBlank(message = "Nome de documento inválido!")
        String nome,
        @NotBlank(message = "Tipo de documento inválido!")
        String tipo,
        @NotNull(message = "Documento sem corpo!")
        String doc
) {}
