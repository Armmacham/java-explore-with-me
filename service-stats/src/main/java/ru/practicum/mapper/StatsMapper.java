package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.controller.dto.HitRequestDto;
import ru.practicum.controller.dto.ViewStatsResponseDto;
import ru.practicum.dao.StatHitCount;
import ru.practicum.dao.StatsEntity;

@Component
public class StatsMapper {

    public ViewStatsResponseDto toViewStatsResponseDto(StatHitCount statHitCount) {
        return new ViewStatsResponseDto(
                statHitCount.getUri(),
                statHitCount.getApp(),
                statHitCount.getHits()
        );
    }

    public StatsEntity toStatsEntity(HitRequestDto hitRequestDto) {
        return new StatsEntity(
                null,
                hitRequestDto.getApp(),
                hitRequestDto.getUri(),
                hitRequestDto.getIp(),
                hitRequestDto.getTimestamp(),
                null
        );
    }
}
