package ru.practicum.controller.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HitRequestDto {
    private String app;
    private String url;
    private String ip;
    private LocalDateTime timestamp;
}
