package ru.practicum.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends CrudRepository<StatsEntity, Long> {

    @Query(nativeQuery = true, name = "WithoutUriAndNotUnique")
    List<StatHitCount> findAllWithoutUriAndNotUnique(LocalDateTime start, LocalDateTime end);

    @Query(nativeQuery = true, name = "WithoutUriAndUnique")
    List<StatHitCount> findAllWithoutUriAndUnique(LocalDateTime start, LocalDateTime end);

    @Query(nativeQuery = true, name = "WithUrisAndNotUnique")
    List<StatHitCount> findAllWithUrisAndNotUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(nativeQuery = true, name = "WithUrisAndUnique")
    List<StatHitCount> findAllWithUrisAndUnique(LocalDateTime start, LocalDateTime end, List<String> uris);
}
