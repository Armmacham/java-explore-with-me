package ru.practicum.ewmservice.rating.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.ewmservice.event.dao.EventEntity;

@Data
@AllArgsConstructor
public class Rating {
    private EventEntity event;
    private Long likesCount;
    private Long dislikeCount;
}
