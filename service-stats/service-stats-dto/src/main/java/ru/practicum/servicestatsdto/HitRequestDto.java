package ru.practicum.servicestatsdto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
public class HitRequestDto {
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp;
}
