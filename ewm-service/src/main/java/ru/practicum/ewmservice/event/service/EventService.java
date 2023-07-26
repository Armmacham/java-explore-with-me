package ru.practicum.ewmservice.event.service;


import ru.practicum.ewmservice.event.controller.dto.EventFullDto;
import ru.practicum.ewmservice.event.controller.dto.EventShortDto;
import ru.practicum.ewmservice.event.controller.dto.NewEventDto;
import ru.practicum.ewmservice.event.dao.EventEntity;
import ru.practicum.ewmservice.state.SortState;
import ru.practicum.ewmservice.state.State;
import ru.practicum.ewmservice.user.controller.dto.UpdateEventUserRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventFullDto createEvent(Long userId, NewEventDto eventDto);

    EventEntity getById(Long eventId);

    List<EventEntity> getEventListByEventIds(List<Long> ids);

    EventFullDto saveChangeEventForAdmin(EventEntity eventEntity);

    List<EventShortDto> getEventShortListWithSort(List<EventEntity> events, Boolean onlyAvailable);

    EventFullDto getFullEventById(Long id, String ip);

    EventFullDto updateEvent(Long userId, Long eventId,
                                   UpdateEventUserRequest eventUserRequest);

    EventFullDto findFullInfoEventByCreator(Long userId, Long eventId);

    List<EventFullDto> findEventsWithParameters(List<Long> users, List<State> states,
                                                      List<Long> categories, LocalDateTime rangeStart,
                                                      LocalDateTime rangeEnd, int from, int size);

    List<EventShortDto> findEventsByCurrentUser(Long userId, int from, int size);

    List<EventShortDto> findPublicEventsWithParameters(String text, List<Long> categories,
                                                             Boolean paid, String rangeStart, String rangeEnd,
                                                             Boolean onlyAvailable, SortState sort, int from,
                                                             int size, String ip);
}
