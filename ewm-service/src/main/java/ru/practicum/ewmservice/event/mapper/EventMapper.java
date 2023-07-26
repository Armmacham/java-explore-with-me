package ru.practicum.ewmservice.event.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewmservice.admin.controller.dto.UpdateEventAdminRequest;
import ru.practicum.ewmservice.category.dao.CategoryEntity;
import ru.practicum.ewmservice.category.mapper.CategoryMapper;
import ru.practicum.ewmservice.event.controller.dto.EventFullDto;
import ru.practicum.ewmservice.event.controller.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewmservice.event.controller.dto.EventShortDto;
import ru.practicum.ewmservice.event.controller.dto.NewEventDto;
import ru.practicum.ewmservice.event.dao.EventEntity;
import ru.practicum.ewmservice.location.dao.LocationEntity;
import ru.practicum.ewmservice.request.mapper.RequestMapper;
import ru.practicum.ewmservice.request.dao.RequestEntity;
import ru.practicum.ewmservice.user.dao.UserEntity;
import ru.practicum.ewmservice.user.mapper.UserMapper;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventMapper {

    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;
    private final RequestMapper requestMapper;

    public EventFullDto toEventFullDto(EventEntity event, Long confirmedRequests, Long views) {
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setConfirmedRequests(confirmedRequests == null ? 0L : confirmedRequests);
        eventFullDto.setViews(views == null ? 0L : views);
        eventFullDto.setId(event.getId());
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setState(event.getState());
        eventFullDto.setCategory(categoryMapper.toDto(event.getCategory()));
        eventFullDto.setPublishedOn(event.getPublishedOn());
        eventFullDto.setInitiator(userMapper.toDto(event.getInitiator()));
        eventFullDto.setLocation(event.getLocation());
        eventFullDto.setPaid(event.isPaid());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setCreatedOn(event.getCreatedOn());
        eventFullDto.setEventDate(event.getEventDate());
        eventFullDto.setRequestModeration(event.isRequestModeration());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        return eventFullDto;
    }

    public EventEntity toEntity(UserEntity user, CategoryEntity category, LocationEntity location, NewEventDto newEventDto) {
        EventEntity event = new EventEntity();
        event.setAnnotation(newEventDto.getAnnotation());
        event.setCategory(category);
        event.setCreatedOn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        event.setDescription(newEventDto.getDescription());
        event.setEventDate(newEventDto.getEventDate());
        event.setInitiator(user);
        event.setParticipantLimit(newEventDto.getParticipantLimit());
        event.setLocation(location);
        event.setRequestModeration(newEventDto.isRequestModeration());
        event.setTitle(newEventDto.getTitle());
        event.setPaid(newEventDto.isPaid());
        return event;
    }

    public EventRequestStatusUpdateResult toEventRequestStatusUpdateResult(List<RequestEntity> acceptRequest,
                                                                           List<RequestEntity> rejectRequest) {
        return new EventRequestStatusUpdateResult(
                acceptRequest.stream().map(requestMapper::toDto).collect(Collectors.toList()),
                rejectRequest.stream().map(requestMapper::toDto).collect(Collectors.toList()));
    }

    public NewEventDto toUpdateDto(UpdateEventAdminRequest updateEvent) {
        NewEventDto newEventDto = new NewEventDto();
        newEventDto.setAnnotation(updateEvent.getAnnotation());
        newEventDto.setDescription(updateEvent.getDescription());
        newEventDto.setPaid(updateEvent.isPaid());
        newEventDto.setLocation(updateEvent.getLocation());
        newEventDto.setCategory(updateEvent.getCategory());
        newEventDto.setParticipantLimit(updateEvent.getParticipantLimit());
        newEventDto.setRequestModeration(updateEvent.isRequestModeration());
        newEventDto.setTitle(updateEvent.getTitle());
        newEventDto.setEventDate(updateEvent.getEventDate());
        return newEventDto;
    }

    public EventShortDto toEventShortDto(EventEntity event, Long confirmedRequest, Long views) {
        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setId(event.getId());
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setEventDate(event.getEventDate());
        eventShortDto.setPaid(event.isPaid());
        eventShortDto.setConfirmedRequests(confirmedRequest);
        eventShortDto.setViews(views);
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setCategory(categoryMapper.toDto(event.getCategory()));
        eventShortDto.setInitiator(userMapper.toShortDto(event.getInitiator()));
        return eventShortDto;
    }

}
