package poli.csi.projeto_integrador.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poli.csi.projeto_integrador.dto.filter.FilterEvent;
import poli.csi.projeto_integrador.dto.request.ContributionDto;
import poli.csi.projeto_integrador.dto.request.EventDto;
import poli.csi.projeto_integrador.dto.request.EventStatusDto;
import poli.csi.projeto_integrador.model.Event;
import poli.csi.projeto_integrador.service.EventService;

@RestController
@AllArgsConstructor
@RequestMapping("/event")
public class EventController {
    private final EventService eventService;

    @PostMapping("/save")
    public ResponseEntity<String> saveEvent(@Valid @RequestBody EventDto dto, @RequestHeader("timezone") String timezone) {
        boolean res = eventService.saveEvent(dto, timezone);
        if (res) {
            return ResponseEntity.ok("Evento cadastrado com sucesso!");
        }
        return ResponseEntity.internalServerError().body("Erro ao cadastrar evento!");
    }

    @PutMapping("/update/status")
    public ResponseEntity<String> updateEventStatus(
            @RequestBody EventStatusDto dto,
            @RequestHeader("timezone") String timezone
    ) {
        boolean res = eventService.updateEventStatus(dto, timezone);
        if (res) {
            return ResponseEntity.ok("Status do evento alterado com sucesso!");
        }
        return ResponseEntity.internalServerError().body("Erro ao alterar status do evento evento!");
    }

    @PutMapping("/update/contribute")
    public ResponseEntity<String> contributeToEvent(@RequestBody ContributionDto dto) {
        boolean res = eventService.contributeToEvent(dto);
        if (res) {
            return ResponseEntity.ok("Aporte de valores efetuado com sucesso ao evento!");
        }
        return ResponseEntity.internalServerError().body("Erro ao efetuar aporte de valores ao evento!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEvent(@NotNull(message = "Id do evento inválido!") @PathVariable("id") Long idEvent) {
        Event res = eventService.getEvent(idEvent);
        if (res != null) {
            return ResponseEntity.ok(res);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<Page<Event>> getEvents(
            @NotNull(message = "Id de usuário inválido!") @PathVariable("id") Long idUser,
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "periodicity", required = false) String periodicity,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "archived") Boolean archived
    ) {
        FilterEvent filter = FilterEvent.builder()
                .name(name)
                .periodicity(periodicity)
                .archived(archived)
                .status(status)
                .type(type)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        Page<Event> events = eventService.getAllEvents(filter, pageable, idUser);
        return ResponseEntity.ok(events);
    }

}
