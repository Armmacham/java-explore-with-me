package ru.practicum.ewmservice.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.category.mapper.CategoryMapper;
import ru.practicum.ewmservice.category.service.CategoryService;
import ru.practicum.ewmservice.event.controller.dto.EventFullDto;
import ru.practicum.ewmservice.event.controller.dto.EventShortDto;
import ru.practicum.ewmservice.event.controller.dto.EventWithRequestNum;
import ru.practicum.ewmservice.event.controller.dto.NewEventDto;
import ru.practicum.ewmservice.event.dao.EventEntity;
import ru.practicum.ewmservice.event.mapper.EventMapper;
import ru.practicum.ewmservice.event.repository.AdminEventRepository;
import ru.practicum.ewmservice.event.repository.EventRepository;
import ru.practicum.ewmservice.exception.EntityNotFoundException;
import ru.practicum.ewmservice.exception.InvalidStateException;
import ru.practicum.ewmservice.location.dao.LocationEntity;
import ru.practicum.ewmservice.location.service.LocationService;
import ru.practicum.ewmservice.state.NewEventState;
import ru.practicum.ewmservice.state.RequestState;
import ru.practicum.ewmservice.state.SortState;
import ru.practicum.ewmservice.state.State;
import ru.practicum.ewmservice.user.controller.dto.UpdateEventUserRequest;
import ru.practicum.ewmservice.user.dao.UserEntity;
import ru.practicum.ewmservice.user.service.UserService;
import ru.practicum.servicestatsclient.ServiceStatsClient;
import ru.practicum.servicestatsdto.HitRequestDto;
import ru.practicum.servicestatsdto.ViewStatsResponseDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {

    private final UserService userService;
    private final CategoryService categoryService;
    private final LocationService locationService;
    private final EventRepository eventRepository;
    private final ServiceStatsClient statsClient;
    private final AdminEventRepository adminEventRepository;

    private final CategoryMapper categoryMapper;
    private final EventMapper eventMapper;

    private final String app = "evm-service";

    private final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public EventFullDto createEvent(Long userId, NewEventDto eventDto) {
        UserEntity user = userService.getById(userId);
        if (eventDto.getEventDate() != null && eventDto.getEventDate().isBefore(LocalDateTime.now())) {
            throw new InvalidStateException("invalid event date");
        }
        EventEntity event = eventMapper.toEntity(
                user,
                categoryMapper.toEntity(categoryService.getById(eventDto.getCategory())),
                locationService.save(eventDto.getLocation()),
                eventDto);
        event.setState(State.PENDING);
        return eventMapper.toEventFullDto(eventRepository.save(event), 0L, 0L);
    }

    @Override
    public List<EventEntity> getEventListByEventIds(List<Long> ids) {
        return eventRepository.getEventsByIdIn(ids);
    }

    @Override
    public EventEntity getById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("event with id=" + eventId + " not found"));
    }

    @Override
    public EventFullDto saveChangeEventForAdmin(EventEntity event) {
        event.setPublishedOn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        EventEntity resultEven = eventRepository.save(event);
        return eventMapper.toEventFullDto(resultEven, 0L, 0L);
    }

    @Override
    public EventFullDto getFullEventById(Long id, String ip) {
        EventEntity event = getById(id);

        statsClient.hit(new HitRequestDto(app, "/events/" + id, ip, LocalDateTime.now()));

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new InvalidStateException("event not in published state");
        }

        return getEventFullDto(event.getId(), event, List.of(event.getId()));
    }

    @Override
    public EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest eventUserRequest) {
        userService.getById(userId);
        EventEntity event = getById(eventId);
        if (!Objects.equals(userId, event.getInitiator().getId())) {
            throw new InvalidStateException("user cannot change another user event");
        }
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new InvalidStateException("cannot change event, incorrect date");
        }
        if (eventUserRequest.getEventDate() != null && eventUserRequest.getEventDate().isBefore(LocalDateTime.now())) {
            throw new InvalidStateException("cannot change event, incorrect date");
        }
        if (event.getState().equals(State.PUBLISHED)) {
            throw new InvalidStateException("cannot change event, its already published");
        }
        return setNewParamsForEvent(event, eventUserRequest);
    }

    @Override
    public EventFullDto findFullInfoEventByCreator(Long userId, Long eventId) {
        userService.getById(userId);
        EventEntity event = getById(eventId);
        if (!Objects.equals(userId, event.getInitiator().getId())) {
            throw new InvalidStateException("user cannot view another user event");
        }

        return getEventFullDto(eventId, event, List.of(eventId));
    }

    @Override
    public List<EventFullDto> findEventsWithParameters(List<Long> users, List<State> states,
                                                       List<Long> categories, LocalDateTime rangeStart,
                                                       LocalDateTime rangeEnd, int from, int size) {

        List<EventEntity> events =
                adminEventRepository.findEvents(users, states, categories, rangeStart, rangeEnd, from / size, size);

        Map<Long, Long> confirmedRequestMap =
                findEventIdConfirmedCount(events.stream().map(EventEntity::getId).collect(Collectors.toList()));

        Map<Long, Long> viewsMap = getViews(events, events.stream().map(e -> "/events/" + e.getId()).collect(Collectors.toList()));

        return events
                .stream()
                .map(event -> eventMapper.toEventFullDto(event,
                        confirmedRequestMap.getOrDefault(event.getId(), 0L),
                        viewsMap.getOrDefault(event.getId(), 0L)))
                .collect(Collectors.toList());
    }

    @Override
    public List<EventShortDto> findEventsByCurrentUser(Long userId, int from, int size) {
        userService.getById(userId);

        Page<EventEntity> userEvents = eventRepository.findAllUserEvents(userId, PageRequest.of(from / size, size));

        Map<Long, Long> confirmedRequestMap = findEventIdConfirmedCount(
                userEvents.stream().map(EventEntity::getId).collect(Collectors.toList()));
        Map<Long, Long> viewsMap = getViews(
                userEvents.toList(),
                userEvents.stream().map(e -> "/events/" + e.getId()).collect(Collectors.toList()));

        return userEvents
                .stream()
                .map(event -> eventMapper
                        .toEventShortDto(event, confirmedRequestMap.getOrDefault(event.getId(), 0L),
                                viewsMap.getOrDefault(event.getId(), 0L)))
                .sorted(Comparator.comparing(EventShortDto::getViews))
                .collect(Collectors.toList());
    }

    public List<EventShortDto> findPublicEventsWithParameters(String text, List<Long> categories,
                                                              Boolean paid, String rangeStart, String rangeEnd,
                                                              Boolean onlyAvailable, SortState sort, int from,
                                                              int size, String ip) {

        statsClient.hit(new HitRequestDto(app, "/events", ip, LocalDateTime.now()));

        if (rangeStart == null) {
            rangeStart = LocalDateTime.now().format(dateTimeFormatter);
        }
        List<EventEntity> events = adminEventRepository.findPublicEventsWithParameters(text, categories, paid, rangeStart,
                rangeEnd, sort, from, size);
        List<EventShortDto> eventsShortDto = getEventShortListWithSort(events, onlyAvailable);

        if (sort != null && sort.equals(SortState.VIEWS)) {
            return eventsShortDto.stream()
                    .sorted(Comparator.comparing(EventShortDto::getViews))
                    .collect(Collectors.toList());
        }

        return eventsShortDto;
    }


    private EventFullDto setNewParamsForEvent(EventEntity event, UpdateEventUserRequest eventUserRequest) {
        Map<Long, Long> viewsMap = getViews(List.of(event), List.of("/events/" + event.getId()));
        Map<Long, Long> confirmedRequestMap = findEventIdConfirmedCount(List.of(event.getId()));

        if (eventUserRequest.getStateAction().equals(NewEventState.CANCEL_REVIEW)) {
            event.setState(State.CANCELED);
            EventEntity resultEvent = eventRepository.save(event);
            return eventMapper.toEventFullDto(resultEvent,
                    confirmedRequestMap.getOrDefault(event.getId(), 0L),
                    viewsMap.getOrDefault(event.getId(), 0L));
        } else {
            event.setState(State.PENDING);
        }
        if (eventUserRequest.getRequestModeration() != null) {
            event.setRequestModeration(eventUserRequest.getRequestModeration());
        }
        if (eventUserRequest.getDescription() != null) {
            event.setDescription(eventUserRequest.getDescription());
        }
        if (eventUserRequest.getCategory() != null) {
            event.setCategory(categoryMapper.toEntity(categoryService
                    .getById(eventUserRequest.getCategory())));
        }
        if (eventUserRequest.getEventDate() != null) {
            event.setEventDate(eventUserRequest.getEventDate());
        }
        if (eventUserRequest.getAnnotation() != null) {
            event.setAnnotation(eventUserRequest.getAnnotation());
        }
        if (eventUserRequest.getTitle() != null) {
            event.setTitle(eventUserRequest.getTitle());
        }
        if (eventUserRequest.getPaid() != null) {
            event.setPaid(eventUserRequest.getPaid());
        }
        if (eventUserRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(eventUserRequest.getParticipantLimit());
        }
        if (eventUserRequest.getLocation() != null &&
                !Objects.equals(event.getLocation().getLon(), eventUserRequest.getLocation().getLon()) &&
                !Objects.equals(event.getLocation().getLat(), eventUserRequest.getLocation().getLat())) {
            locationService.delete(event.getLocation());
            LocationEntity newLoc = locationService.save(eventUserRequest.getLocation());
            event.setLocation(newLoc);
        }

        EventEntity eventResult = eventRepository.save(event);
        return eventMapper.toEventFullDto(eventResult,
                confirmedRequestMap.getOrDefault(event.getId(), 0L),
                viewsMap.getOrDefault(event.getId(), 0L));
    }

    @Override
    public List<EventShortDto> getEventShortListWithSort(List<EventEntity> events, Boolean onlyAvailable) {
        Map<Long, Long> viewsMap = getViews(events, events.stream().map(e -> "/events/" + e.getId()).collect(Collectors.toList()));
        Map<Long, Long> confirmedRequestMap = findEventIdConfirmedCount(events.stream().map(EventEntity::getId).collect(Collectors.toList()));

        if (onlyAvailable) {
            return events
                    .stream()
                    .filter(event -> event.getParticipantLimit() > confirmedRequestMap.get(event.getId()))
                    .map(event -> eventMapper.toEventShortDto(event, confirmedRequestMap.getOrDefault(event.getId(), 0L), viewsMap.getOrDefault(event.getId(), 0L)))
                    .collect(Collectors.toList());
        }
        return events.stream()
                .map(event -> eventMapper.toEventShortDto(event, confirmedRequestMap.getOrDefault(event.getId(), 0L),
                        viewsMap.getOrDefault(event.getId(), 0L)))
                .collect(Collectors.toList());
    }

    private Map<Long, Long> findEventIdConfirmedCount(List<Long> eventIds) {
        Collection<EventWithRequestNum> resultConfirmedReq = eventRepository
                .getConfirmedRequestMap(eventIds, RequestState.CONFIRMED.toString());

        return resultConfirmedReq
                .stream()
                .collect(Collectors.toMap(EventWithRequestNum::getEventId, EventWithRequestNum::getConfirmedRequestSize));
    }

    private Map<Long, Long> getViews(List<EventEntity> events, List<String> uris) {
        LocalDateTime startTime = getStartTimeForStatistic(events).minusMinutes(1);
        Map<Long, Long> resultViewsMap = new HashMap<>();
        Collection<ViewStatsResponseDto> stats = statsClient.showStats(startTime, LocalDateTime.now().plusMinutes(1)
                .truncatedTo(ChronoUnit.SECONDS), uris, false);
        stats.forEach(statsOutputDto -> resultViewsMap.put(
                Long.parseLong(Arrays.stream(statsOutputDto.getUri().split("/")).collect(Collectors.toList()).get(2)),
                statsOutputDto.getHits()));
        return resultViewsMap;
    }

    private LocalDateTime getStartTimeForStatistic(List<EventEntity> events) {
        List<LocalDateTime> timePublishedEvent = new ArrayList<>();
        List<LocalDateTime> finalTimePublishedEvent;

        events.forEach(event -> {
            if (event.getPublishedOn() != null) {
                timePublishedEvent.add(event.getPublishedOn());
            }
        });
        if (timePublishedEvent.size() == 0) {
            return LocalDateTime.now().minusMinutes(1).truncatedTo(ChronoUnit.SECONDS);
        }
        finalTimePublishedEvent = timePublishedEvent.stream().sorted(Comparator
                .comparing(LocalDateTime::getDayOfYear).reversed()).collect(Collectors.toList());
        return finalTimePublishedEvent.get(0);
    }

    private EventFullDto getEventFullDto(Long eventId, EventEntity event, List<Long> eventIds) {
        Map<Long, Long> confirmedRequestMap = findEventIdConfirmedCount(eventIds);
        Map<Long, Long> viewsMap = getViews(List.of(event), List.of("/events/" + eventId));
        return eventMapper.toEventFullDto(event, confirmedRequestMap.getOrDefault(eventId, 0L),
                viewsMap.getOrDefault(eventId, 0L));
    }
}
