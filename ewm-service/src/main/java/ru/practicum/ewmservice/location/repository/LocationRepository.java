package ru.practicum.ewmservice.location.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmservice.location.dao.LocationEntity;

@Repository
public interface LocationRepository extends CrudRepository<LocationEntity, Long> {
}
