package ru.practicum.ewmservice.user.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserShortDto {
    private Long id;
    private String name;
}
