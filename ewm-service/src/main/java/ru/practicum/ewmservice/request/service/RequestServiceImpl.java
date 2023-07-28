package ru.practicum.ewmservice.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.event.controller.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewmservice.event.controller.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewmservice.event.dao.EventEntity;
import ru.practicum.ewmservice.event.mapper.EventMapper;
import ru.practicum.ewmservice.event.service.EventService;
import ru.practicum.ewmservice.exception.EntityNotFoundException;
import ru.practicum.ewmservice.exception.InvalidStateException;
import ru.practicum.ewmservice.request.controller.dto.ParticipationRequestDto;
import ru.practicum.ewmservice.request.mapper.RequestMapper;
import ru.practicum.ewmservice.request.dao.RequestEntity;
import ru.practicum.ewmservice.request.repository.RequestRepository;
import ru.practicum.ewmservice.state.RequestState;
import ru.practicum.ewmservice.state.State;
import ru.practicum.ewmservice.user.dao.UserEntity;
import ru.practicum.ewmservice.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final EventService eventService;
    private final UserService userService;
    private final RequestMapper requestMapper;
    private final EventMapper eventMapper;

    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        UserEntity user = userService.getById(userId);
        EventEntity event = eventService.getById(eventId);
        checkRequest(user, event);
        RequestEntity request = new RequestEntity();
        request.setRequester(user);
        request.setCreated(LocalDateTime.now());
        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(RequestState.CONFIRMED);
        } else {
            request.setStatus(RequestState.PENDING);
        }
        request.setEvent(event);
        return requestMapper.toDto(requestRepository.save(request));
    }

    @Override
    public List<ParticipationRequestDto> getParticipationsList(Long userId, Long eventId) {
        UserEntity user = userService.getById(userId);
        EventEntity event = eventService.getById(eventId);
        if (!user.getId().equals(event.getInitiator().getId())) {
            throw new InvalidStateException("user " + userId + " is not initiator");
        }
        return requestRepository.getRequestsByEvent(event)
                .stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        UserEntity user = userService.getById(userId);
        return requestRepository.getRequestsByRequester(user)
                .stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        UserEntity user = userService.getById(userId);
        RequestEntity request = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("request with id=" + requestId + " not found"));
        if (!user.getId().equals(request.getRequester().getId())) {
            throw new InvalidStateException("user " + userId + " not created this request");
        }
        request.setStatus(RequestState.CANCELED);
        return requestMapper.toDto(requestRepository.save(request));
    }

    @Override
    public EventRequestStatusUpdateResult changeStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest requests) {
        UserEntity user = userService.getById(userId);
        EventEntity event = eventService.getById(eventId);
        if (!user.getId().equals(event.getInitiator().getId())) {
            throw new InvalidStateException("user " + userId + " is not initiator");
        }

        int confirmedReqNow = requestRepository.getConfirmedRequest(eventId, RequestState.CONFIRMED);
        if (event.getParticipantLimit() == confirmedReqNow) {
            throw new InvalidStateException("requests limit");
        }

        List<RequestEntity> allRequests = requestRepository.findAllByIdIn(requests.getRequestIds());

        allRequests
                .stream()
                .filter(e -> !RequestState.PENDING.equals(e.getStatus()))
                .findAny()
                .ifPresent(s -> {
                    throw new IllegalStateException("illegal state in request");
                });

        if (RequestState.REJECTED.equals(requests.getStatus())) {
            return rejectAll(allRequests);
        }

        return confirmOrRejectRequests(confirmedReqNow, allRequests, event);
    }

    private void checkRequest(UserEntity user, EventEntity event) {
        Optional<RequestEntity> requestByRequesterAndEvent = requestRepository.getRequestByRequesterAndEvent(user, event);
        if (requestByRequesterAndEvent.isPresent()) {
            throw new InvalidStateException("request with requestor " + user.getId() +
                    " and event " + event.getId() + " already exists");
        }
        if (Objects.equals(user.getId(), event.getInitiator().getId())) {
            throw new InvalidStateException("user is organizator");
        }
        if (State.CANCELED.equals(event.getState()) || State.PENDING.equals(event.getState())) {
            throw new InvalidStateException("event in invalid status");
        }
        if (event.getParticipantLimit() != 0 &&
                event.getParticipantLimit() <= requestRepository.getConfirmedRequest(event.getId(), RequestState.CONFIRMED)) {
            throw new InvalidStateException("participants limit");
        }
    }

    private EventRequestStatusUpdateResult rejectAll(List<RequestEntity> allRequests) {
        allRequests.forEach(request -> {
            request.setStatus(RequestState.REJECTED);
            requestRepository.save(request);
        });
        return eventMapper.toEventRequestStatusUpdateResult(new ArrayList<>(), allRequests);
    }

    private EventRequestStatusUpdateResult confirmOrRejectRequests(Integer confirmed,
                                                                   List<RequestEntity> requests,
                                                                   EventEntity event) {
        List<RequestEntity> accepted = requests
                .stream()
                .limit(event.getParticipantLimit() - confirmed)
                .map(r -> {
                    r.setStatus(RequestState.CONFIRMED);
                    return requestRepository.save(r);
                })
                .collect(Collectors.toList());

        List<RequestEntity> rejected = requests
                .stream()
                .skip(accepted.size())
                .map(r -> {
                    r.setStatus(RequestState.REJECTED);
                    return requestRepository.save(r);
                }).collect(Collectors.toList());

        return eventMapper.toEventRequestStatusUpdateResult(accepted, rejected);
    }
}
