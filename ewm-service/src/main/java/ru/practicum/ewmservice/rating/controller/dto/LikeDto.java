package ru.practicum.ewmservice.rating.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class LikeDto {
    private Long id;
    private boolean value;
}
