package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.exception.InvalidTimeException;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.controller.dto.HitRequestDto;
import ru.practicum.controller.dto.ViewStatsResponseDto;
import ru.practicum.dao.StatsEntity;
import ru.practicum.dao.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {

    private final StatsMapper statMapper;
    private final StatsRepository statsRepository;

    @Override
    public void hit(HitRequestDto hitRequestDto) {
        StatsEntity statsEntity = statMapper.toStatsEntity(hitRequestDto);
        statsRepository.save(statsEntity);
    }

    @Override
    public List<ViewStatsResponseDto> showStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        checkTime(start, end);
        if (uris == null || uris.isEmpty()) {
            if (unique) {
                return statsRepository.findAllWithoutUriAndUnique(start, end)
                        .stream().map(statMapper::toViewStatsResponseDto).collect(Collectors.toList());
            }
            return statsRepository.findAllWithoutUriAndNotUnique(start, end)
                    .stream().map(statMapper::toViewStatsResponseDto).collect(Collectors.toList());
        } else {
            if (unique) {
                return statsRepository.findAllWithUrisAndUnique(start, end, uris)
                        .stream().map(statMapper::toViewStatsResponseDto).collect(Collectors.toList());
            }
            return statsRepository.findAllWithUrisAndNotUnique(start, end, uris)
                    .stream().map(statMapper::toViewStatsResponseDto).collect(Collectors.toList());
        }
    }

    private void checkTime(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start) || start.isAfter(LocalDateTime.now())) {
            log.info("Invalid interval from " + start + " to " + end);
            throw new InvalidTimeException("Invalid interval from " + start + " to " + end);
        }
    }
}
