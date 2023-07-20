package ru.practicum.service;

import ru.practicum.controller.dto.HitRequestDto;
import ru.practicum.controller.dto.ViewStatsResponseDto;

import java.util.List;

public interface StatsService {
    void hit(HitRequestDto hitRequestDto);

    List<ViewStatsResponseDto> showStats();
}
