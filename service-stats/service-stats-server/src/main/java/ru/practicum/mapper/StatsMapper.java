package ru.practicum.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dao.AppEntity;
import ru.practicum.dao.StatHitCount;
import ru.practicum.dao.StatsEntity;
import ru.practicum.servicestatsdto.HitRequestDto;
import ru.practicum.servicestatsdto.ViewStatsResponseDto;

@Component
public class StatsMapper {

    public ViewStatsResponseDto toViewStatsResponseDto(StatHitCount statHitCount) {
        return new ViewStatsResponseDto(
                statHitCount.getUrl(),
                statHitCount.getApp(),
                statHitCount.getHits()
        );
    }

    public StatsEntity toStatsEntity(HitRequestDto hitRequestDto, AppEntity app) {
        return new StatsEntity(
                null,
                app,
                hitRequestDto.getUri(),
                hitRequestDto.getIp(),
                hitRequestDto.getTimestamp(),
                null
        );
    }
}
