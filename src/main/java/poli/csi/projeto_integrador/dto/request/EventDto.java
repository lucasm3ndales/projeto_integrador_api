package poli.csi.projeto_integrador.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public record EventDto(
        @NotBlank(message = "Nome do evento inválido!")
        @Size(max = 125, message = "Nome do evento muito longo!")
        String name,
        @NotBlank(message = "Tipo do evento inválido!")
        @Size(max = 63, message = "Tipo do evento muito longo!")
        String type,
        @NotBlank(message = "Periodicidade do evento inválida!")
        @Size(max = 63, message = "Periodicidade do evento muito longa!")
        String periodicity,
        @NotBlank(message = "Data de início do evento inválida!")
        @Size(message = "Data de início do evento muito longa!")
        String startDate,
        @NotBlank(message = "Data de fim do evento inválida!")
        @Size(message = "Data de fim do evento muito longa!")
        String endDate,
        @NotBlank(message = "Data de ida do evento inválida!")
        @Size(message = "Data de ida do evento muito longa!")
        String departureDate,
        @NotBlank(message = "Data de volta do evento inválida!")
        @Size(message = "Data de volta do evento muito longa!")
        String backDate,
        @NotBlank(message = "Justificativa do evento inválida!")
        @Size(max = 500, message = "Justificativa do evento muito longa!")
        String goal,
        @NotNull(message = "Número de participantes inválidos!")
        Integer participants,
        @NotNull(message = "Custo do evento inválido!")
        @Min(0)
        @Positive
        BigDecimal cost,
        @Valid
        AddressDto address,
        List<DocumentDto> documents,
        List<EventExpenseDto> eventExpenses,
        @NotNull(message = "Origem do trâmite inválida!")
        Long origin,
        @NotNull(message = "Destinatário do trâmite inválido!")
        Long destiny
) {
}
