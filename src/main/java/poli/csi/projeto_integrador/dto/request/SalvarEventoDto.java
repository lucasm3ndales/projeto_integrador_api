package poli.csi.projeto_integrador.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import poli.csi.projeto_integrador.exception.CustomException;

import java.math.BigDecimal;
import java.util.Set;

public record SalvarEventoDto(
        @NotBlank(message = "Nome do evento inválido!")
        String nome,
        @NotBlank(message = "Tipo do evento inválido!")
        String tipo,
        @NotBlank(message = "Periodicidade do evento inválida!")
        String periodicidade,
        @NotBlank(message = "Data de início do evento inválida!")
        String dataInicio,
        @NotBlank(message = "Data de fim do evento inválida!")
        String dataFim,
        @NotBlank(message = "Data de ida do evento inválida!")
        String dataIda,
        @NotBlank(message = "Data de retorno do evento inválida!")
        String dataVolta,
        @NotBlank(message = "Objetivo do evento inválido!")
        @Size(max = 500, message = "Objetivo do evento muito extenso!")
        String objetivo,
        @NotNull(message = "Número de participantes do evento inválido!")
        @Positive(message = "Número de participantes do evento negativo!")
        @Min(value = 0, message = "Número de participantes do evento negativo!")
        Integer participantes,
        @NotNull(message = "Custo do evento inválido!")
        @Positive(message = "Custo do evento negativo!")
        @Min(value = 0, message = "Custo do evento negativo!")
        BigDecimal custo,
        @NotBlank(message = "País do evento inválido!")
        String pais,
        @NotBlank(message = "Estado/Província do evento inválida!")
        String estado,
        @NotBlank(message = "Cidade do evento inválida!")
        String cidade,
        @NotBlank(message = "Bairro do evento inválido!")
        String bairro,
        @NotBlank(message = "Rua do evento inválida!")
        String rua,
        @NotBlank(message = "Número do evento inválido!")
        String numero,
        String complemento,
        @Valid
        Set<SalvarDocumentoDto> documentos,
        @Valid
        Set<AdicionarDespesaDto> despesas,
        @NotNull(message = "Id do departamento nulo!")
        Long departamentoId,
        @NotNull(message = "Id do servidor nulo!")
        Long servidorId,
        @NotBlank(message = "Status do trâmite inválido!")
        String statusTramite
        
) {
    public SalvarEventoDto {
        if(complemento != null && complemento.length() > 255) {
                throw new CustomException("Complemento muito extenso!");
        }
    }
}
