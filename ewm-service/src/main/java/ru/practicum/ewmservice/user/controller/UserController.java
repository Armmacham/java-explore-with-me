package ru.practicum.ewmservice.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewmservice.event.controller.dto.EventFullDto;
import ru.practicum.ewmservice.event.controller.dto.EventShortDto;
import ru.practicum.ewmservice.event.controller.dto.NewEventDto;
import ru.practicum.ewmservice.event.service.EventService;
import ru.practicum.ewmservice.user.controller.dto.UpdateEventUserRequest;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collection;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final EventService eventService;

    @GetMapping("/{userId}/events")
    public Collection<EventShortDto> findEventsByCurrentUser(
            @PathVariable Long userId,
            @Min(0) @RequestParam(value = "from", defaultValue = "0", required = false) int from,
            @Min(1) @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        return eventService.findEventsByCurrentUser(userId, from, size);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{userId}/events")
    public EventFullDto createEvent(@PathVariable Long userId,
                                    @RequestBody @Valid NewEventDto eventDto) {
        return eventService.createEvent(userId, eventDto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto findFullInfoEventByCreator(@PathVariable Long userId,
                                                   @PathVariable Long eventId) {
        return eventService.findFullInfoEventByCreator(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @RequestBody @Valid UpdateEventUserRequest eventUserRequest) {
        return eventService.updateEvent(userId, eventId, eventUserRequest);
    }
}
