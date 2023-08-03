package ru.practicum.ewmservice.request.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewmservice.request.controller.dto.ParticipationRequestDto;
import ru.practicum.ewmservice.request.dao.RequestEntity;

@Component
public class RequestMapper {

    public ParticipationRequestDto toDto(RequestEntity request) {
        return new ParticipationRequestDto(
                request.getId(),
                request.getCreated(),
                request.getRequester().getId(),
                request.getEvent().getId(),
                request.getStatus()
        );
    }
}
