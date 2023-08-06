package ru.practicum.ewmservice.location.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public LocationEntity save(LocationDto locationDto) {
        log.info("Save location {}", locationDto);
        return locationRepository.findByLatIsAndLonIs(locationDto.getLat(), locationDto.getLon())
                .orElse(locationRepository.save(locationMapper.toEntity(locationDto)));
    }

    @Override
    @Transactional
    public void delete(LocationEntity locationEntity) {
        log.info("Delete location with id {}", locationEntity.getId());
        locationRepository.delete(locationEntity);
    }

}
