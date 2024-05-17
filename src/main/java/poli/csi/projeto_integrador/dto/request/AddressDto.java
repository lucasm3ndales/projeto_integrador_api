package poli.csi.projeto_integrador.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressDto(
        @NotBlank(message = "País inválido!")
        @Size(max = 100, message = "País muito longo!")
        String country,
        @NotBlank(message = "Estado inválido!")
        @Size(max = 8, message = "Estado muito longo!")
        String state,
        @NotBlank(message = "Cidade inválida!")
        @Size(max = 100, message = "Cidade muito longa!")
        String city,
        @NotBlank(message = "Bairro inválido!")
        @Size(max = 100, message = "Bairro muito longo!")
        String district,
        @NotBlank(message = "Rua inválida!")
        @Size(max = 100, message = "Rua muito longa!")
        String street,
        String num,
        String complement
) {}
