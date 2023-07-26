package ru.practicum.ewmservice.request.service;

import ru.practicum.ewmservice.event.controller.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewmservice.event.controller.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewmservice.request.controller.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {

    List<ParticipationRequestDto> getUserRequests(Long userId);

    ParticipationRequestDto createRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);

    List<ParticipationRequestDto> getParticipationsList(Long userId, Long eventId);

    EventRequestStatusUpdateResult changeStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest requests);
}
