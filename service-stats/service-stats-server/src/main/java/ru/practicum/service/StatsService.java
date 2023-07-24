package ru.practicum.service;

import ru.practicum.servicestatsdto.HitRequestDto;
import ru.practicum.servicestatsdto.ViewStatsResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    void hit(HitRequestDto hitRequestDto);

    List<ViewStatsResponseDto> showStats(LocalDateTime start, LocalDateTime end, List<String> urls, boolean unique);
}
