package poli.csi.projeto_integrador.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import poli.csi.projeto_integrador.dto.filter.FilterEvent;
import poli.csi.projeto_integrador.dto.request.ContributionDto;
import poli.csi.projeto_integrador.dto.request.EventDto;
import poli.csi.projeto_integrador.dto.request.EventStatusDto;
import poli.csi.projeto_integrador.exception.CustomException;
import poli.csi.projeto_integrador.model.Address;
import poli.csi.projeto_integrador.model.Event;
import poli.csi.projeto_integrador.model.Procedure;
import poli.csi.projeto_integrador.model.User;
import poli.csi.projeto_integrador.repository.EventRepository;
import poli.csi.projeto_integrador.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;

@Service
@AllArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final ProcedureService procedureService;
    private final ExpenseService expenseService;
    private final UserRepository userRepository;
    private final BudgetService budgetService;

    @Transactional
    public boolean saveEvent(EventDto dto, String timezone) {
        Event.EventType type = null;
        Event.Periodicity periodicity = null;

        try {
            type = Event.EventType.valueOf(dto.type());
            periodicity = Event.Periodicity.valueOf(dto.periodicity());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Tipo ou periodicidade do evento inválidas!");
        }

        if (validateDates(dto.startDate(), dto.endDate())
                || validateDates(dto.startDate(), dto.departureDate())
                || validateDates(dto.departureDate(), dto.endDate())
                || validateDates(dto.backDate(), dto.endDate())
        ) {
            throw new CustomException("Datas para cadastro inválidas!");
        }


        Address address = Address.builder()
                .country(dto.address().country())
                .state(dto.address().state())
                .city(dto.address().city())
                .district(dto.address().district())
                .street(dto.address().street())
                .num(dto.address().num())
                .complement(dto.address().complement())
                .build();
        Event event = Event.builder()
                .name(dto.name())
                .type(type)
                .periodicity(periodicity)
                .startDate(LocalDate.parse(dto.startDate()))
                .endDate(LocalDate.parse(dto.endDate()))
                .departureDate(LocalDate.parse(dto.departureDate()))
                .backDate(LocalDate.parse(dto.backDate()))
                .goal(dto.goal())
                .participants(dto.participants())
                .cost(dto.cost())
                .address(address)
                .contributionDep(BigDecimal.ZERO)
                .contributionReit(BigDecimal.ZERO)
                .status(Event.EventStatus.PENDENTE)
                .archived(false)
                .eventExpenses(new HashSet<>())
                .build();

        eventRepository.save(event);

        User destiny = userRepository.findByUnityId(dto.destiny()).orElseThrow(() -> new EntityNotFoundException("Usuário de destino não encontrado!"));


        boolean resExpense = expenseService.addExpensesToEvent(event, dto.eventExpenses(), timezone);

        if (!resExpense) {
            return false;
        }

        boolean resProcedure = procedureService.process(dto.origin(), destiny, dto.documents(), event, timezone);

        if (!resProcedure) {
            return false;
        }

        eventRepository.save(event);
        return true;
    }

    private boolean validateDates(String s, String e) {
        LocalDate start = LocalDate.parse(s);
        LocalDate end = LocalDate.parse(e);
        return start.isAfter(end);
    }

    //TODO: FIX EXPENSE BUG
    @Transactional
    public boolean updateEventStatus(EventStatusDto dto, String timezone) {
        Event event = eventRepository.findById(dto.eventId())
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado!"));
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado!"));

        Procedure procedure = getLastProcedure(event);

        if(!procedure.getDestiny().equals(user)) {
            throw new CustomException("Usuário não autorizado a realizar modificações!");
        }

        Event.EventStatus status = null;

        try {
            status = Event.EventStatus.valueOf(dto.status());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Status do evento inválido!");
        }

        if (event.getStatus() == Event.EventStatus.PENDENTE && status != Event.EventStatus.PENDENTE && user.getRole() != User.UserType.SERVIDOR) {


            if (status == Event.EventStatus.ACEITO) {
                if (validateCosts(event)) {
                    boolean res = budgetService.decrementBudget(user, event, procedure);

                    if (!res) {
                        throw new CustomException("Erro ao registrar orçamento!");
                    }

                    event.setStatus(Event.EventStatus.ACEITO);
                } else {
                    throw new CustomException("Aportes do evento inválidos!");
                }
            } else {
                event.setStatus(Event.EventStatus.RECUSADO);
            }

            boolean resProcedure = procedureService.process(user, event, timezone);

            if (!resProcedure) {
                throw new CustomException("Erro ao encerrar trâmite!");
            }

            eventRepository.save(event);
        } else {
            throw new CustomException("O status do evento já foi alterado anteriormente!");
        }
        return true;
    }

    private boolean validateCosts(Event event) {
        BigDecimal res = event.getContributionDep().add(event.getContributionReit());
        if (event.getCost().compareTo(res) == 0) {
            return true;
        }
        return false;
    }

    @Transactional
    public boolean contributeToEvent(ContributionDto dto) {

        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado!"));

        Event event = eventRepository.findById(dto.eventId())
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado!"));

        Procedure procedure = getLastProcedure(event);

        if(!procedure.getDestiny().equals(user)) {
            throw new CustomException("Usuário não autorizado a realizar modificações!");
        }

        if (event.getStatus() == Event.EventStatus.PENDENTE && user.getRole() != User.UserType.SERVIDOR) {

            BigDecimal value = event.getContributionDep().add(event.getContributionReit()).add(dto.contribution());

            if (value.compareTo(event.getCost()) == 1) {
                throw new CustomException("O valor total de aporte não pode ser maior que o custo do evento!");
            }

            if (user.getRole() == User.UserType.CHEFE_DEPARTAMENTO) {
                event.setContributionDep(dto.contribution());
            } else {
                event.setContributionReit(dto.contribution());
            }

            eventRepository.save(event);
        }

        return true;
    }

    private Procedure getLastProcedure(Event event) {
        return event.getProcedures().stream()
                .max(Comparator.comparing(Procedure::getCreatedAt))
                .orElseThrow(() -> new EntityNotFoundException("Não há trâmites ligados ao evento!"));
    }

    public Event getEvent(Long idEvent) {
        return eventRepository.findById(idEvent)
                .orElseThrow(() -> new EntityNotFoundException("Evento não encontrado!"));
    }

    public Page<Event> getAllEvents(FilterEvent filter, Pageable pageable, Long idUser) {
        if(isFilter(filter)) {
            Specification<Event> spec = EventRepository.specEvent(filter, idUser);
            return eventRepository.findAll(spec, pageable);
        } else {
            return eventRepository.findEventsByUser(idUser, filter.archived(), pageable);
        }
    }

    private boolean isFilter(FilterEvent e) {
        return e.name() != null && !e.name().isBlank()
                || e.periodicity() != null && !e.periodicity().isBlank()
                || e.status() != null && !e.status().isBlank()
                || e.startDate() != null && !e.startDate().isBlank()
                || e.endDate() != null && !e.endDate().isBlank();
    }
}
