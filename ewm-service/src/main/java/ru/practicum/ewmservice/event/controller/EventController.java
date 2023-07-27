package ru.practicum.ewmservice.event.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewmservice.event.controller.dto.EventFullDto;
import ru.practicum.ewmservice.event.controller.dto.EventShortDto;
import ru.practicum.ewmservice.event.service.EventService;
import ru.practicum.ewmservice.state.SortState;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;
    private final HttpServletRequest request;

    @GetMapping
    public Collection<EventShortDto> findPublicEventsWithParameters(
            @RequestParam(value = "text", required = false) String text,
            @RequestParam(value = "categories", required = false) List<Long> categories,
            @RequestParam(value = "paid", required = false) Boolean paid,
            @RequestParam(value = "rangeStart", required = false) String rangeStart,
            @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
            @RequestParam(value = "onlyAvailable", required = false, defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(value = "sort", defaultValue = "EVENT_DATE") SortState sort,
            @Min(0) @RequestParam(value = "from", defaultValue = "0") int from,
            @Min(1) @RequestParam(value = "size", defaultValue = "10") int size) {
        return eventService.findPublicEventsWithParameters(text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                sort, from, size, request.getRemoteAddr());
    }

    @GetMapping("/{id}")
    public EventFullDto getFullEventById(@PathVariable Long id) {
        return eventService.getFullEventById(id, request.getRemoteAddr());
    }
}
