package ru.practicum.ewmservice.event.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventWithRequestNum {
    private Long eventId;
    private Long confirmedRequestSize;
}
