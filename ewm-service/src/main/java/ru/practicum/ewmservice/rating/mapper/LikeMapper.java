package ru.practicum.ewmservice.rating.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewmservice.event.mapper.EventMapper;
import ru.practicum.ewmservice.rating.controller.dto.EventLikeCount;
import ru.practicum.ewmservice.rating.controller.dto.RatingDto;

@Component
@RequiredArgsConstructor
public class LikeMapper {

    private final EventMapper eventMapper;
    public RatingDto toDto(EventLikeCount eventLikeCount) {
        return new RatingDto(
                eventMapper.toEventShortDto(eventLikeCount.getEvent(), null, null),
                eventLikeCount.getLikesCount(),
                eventLikeCount.getDislikesCount());
    }
}
