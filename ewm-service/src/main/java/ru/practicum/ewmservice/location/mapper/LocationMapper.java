package ru.practicum.ewmservice.location.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewmservice.location.dto.LocationDto;
import ru.practicum.ewmservice.location.dao.LocationEntity;

@Component
public class LocationMapper {

    public LocationEntity toEntity(LocationDto locationDto) {
        return new LocationEntity(null, locationDto.getLon(), locationDto.getLat());
    }

}
