package ru.practicum.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppRepository extends CrudRepository<AppEntity, Long> {
    Optional<AppEntity> findByName(String name);
}
