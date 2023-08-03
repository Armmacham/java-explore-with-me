package ru.practicum.ewmservice.rating.service;

import ru.practicum.ewmservice.rating.controller.dto.RatingDto;

import java.util.List;

public interface RatingService {
    void addReactionOnEvent(Long eventId, Long userId, boolean isLike);

    List<RatingDto> getTopLikesEvents(int from, int size);
}
