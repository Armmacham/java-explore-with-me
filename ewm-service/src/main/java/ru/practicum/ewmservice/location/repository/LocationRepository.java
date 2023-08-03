package ru.practicum.ewmservice.location.repository;

import org.springframework.data.repository.CrudRepository;
import ru.practicum.ewmservice.location.dao.LocationEntity;

import java.util.Optional;

public interface LocationRepository extends CrudRepository<LocationEntity, Long> {
    Optional<LocationEntity> findByLatIsAndLonIs(float lat, float lon);
}
