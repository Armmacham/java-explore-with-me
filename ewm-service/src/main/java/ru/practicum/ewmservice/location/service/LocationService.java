package ru.practicum.ewmservice.location.service;


import ru.practicum.ewmservice.location.dao.LocationEntity;
import ru.practicum.ewmservice.location.dto.LocationDto;

public interface LocationService {

    LocationEntity save(LocationDto locationDto);

    void delete(LocationEntity locationEntity);
}
