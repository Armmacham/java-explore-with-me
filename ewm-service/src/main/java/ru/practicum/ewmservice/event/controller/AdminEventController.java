package ru.practicum.ewmservice.event.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewmservice.event.controller.dto.EventFilterDto;
import ru.practicum.ewmservice.event.controller.dto.UpdateEventAdminRequest;
import ru.practicum.ewmservice.event.controller.dto.EventFullDto;
import ru.practicum.ewmservice.event.service.EventService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
@Validated
public class AdminEventController {

    private final EventService eventService;

    @GetMapping("/events")
    public Collection<EventFullDto> getAllEvents(@Valid EventFilterDto eventFilterDto) {
        return eventService.findEvents(eventFilterDto);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto changeEvent(@PathVariable Long eventId,
                                    @RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest) {

        return eventService.changeEvent(eventId, updateEventAdminRequest);
    }
}
