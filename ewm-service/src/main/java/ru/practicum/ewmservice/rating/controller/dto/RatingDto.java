package ru.practicum.ewmservice.rating.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.ewmservice.event.controller.dto.EventShortDto;

@Data
@AllArgsConstructor
public class RatingDto {
    private EventShortDto event;
    private Long likesCount;
    private Long dislikesCount;
}
