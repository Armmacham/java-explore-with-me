package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.StatsMapper;
import ru.practicum.controller.dto.HitRequestDto;
import ru.practicum.controller.dto.ViewStatsResponseDto;
import ru.practicum.dao.StatsEntity;
import ru.practicum.dao.StatsRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsMapper statMapper;
    private final StatsRepository statsRepository;

    @Override
    public void hit(HitRequestDto hitRequestDto) {
        StatsEntity statsEntity = statMapper.toStatsEntity(hitRequestDto);
        statsRepository.save(statsEntity);
    }

    @Override
    public List<ViewStatsResponseDto> showStats() {
        return statsRepository.findAllCount()
                .stream().map(statMapper::toViewStatsResponseDto)
                .collect(Collectors.toList());
    }
}
