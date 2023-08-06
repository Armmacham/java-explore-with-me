package ru.practicum.ewmservice.rating.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.ewmservice.event.dao.EventEntity;

@Data
@AllArgsConstructor
public class EventLikeCount {
    private EventEntity event;
    private Long likesCount;
    private Long dislikesCount;
}
