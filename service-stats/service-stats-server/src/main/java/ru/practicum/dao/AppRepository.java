package ru.practicum.dao;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AppRepository extends CrudRepository<AppEntity, Long> {
    Optional<AppEntity> findByName(String name);
}
