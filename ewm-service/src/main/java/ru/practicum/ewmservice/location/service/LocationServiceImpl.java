package ru.practicum.ewmservice.location.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.location.dao.LocationEntity;
import ru.practicum.ewmservice.location.dto.LocationDto;
import ru.practicum.ewmservice.location.mapper.LocationMapper;
import ru.practicum.ewmservice.location.repository.LocationRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;

    @Override
    public LocationEntity save(LocationDto locationDto) {
        log.info("save location {}", locationDto);
        return locationRepository.save(locationMapper.toEntity(locationDto));
    }

    @Override
    public void delete(LocationEntity locationEntity) {
        log.info("delete location with id {}", locationEntity.getId());
        locationRepository.delete(locationEntity);
    }

}
