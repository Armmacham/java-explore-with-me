package ru.practicum.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends CrudRepository<StatsEntity, Long> {

    @Query(nativeQuery = true, value = "SELECT s.url, a.name, COUNT(s.url) " +
            "FROM stat s JOIN app a ON a.id = s.app_id " +
            "WHERE s.timestamp > :start AND s.timestamp < :end " +
            "GROUP BY s.url, a.name ORDER BY COUNT(s.url) DESC")
    List<StatHitCount> findAllWithoutUriAndNotUnique(LocalDateTime start, LocalDateTime end);

    @Query(nativeQuery = true, value = "SELECT s.url, a.name, COUNT(DISTINCT s.ip) " +
            "FROM stat s JOIN app a ON a.id = s.app_id " +
            "WHERE s.timestamp > :start AND s.timestamp < :end " +
            "GROUP BY s.url, a.name ORDER BY COUNT(DISTINCT s.ip) DESC")
    List<StatHitCount> findAllWithoutUriAndUnique(LocalDateTime start, LocalDateTime end);

    @Query(nativeQuery = true, value = "select s.url, a.name, count(s.url) from stat s join app a on a.id = s.app_id" +
            " where s.timestamp > :start and s.timestamp < :end and s.url in (:uris) " +
            "group by s.url, a.name order by count(s.url) desc")
    List<StatHitCount> findAllWithUrisAndNotUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(nativeQuery = true, value = "SELECT s.url, a.name, COUNT(DISTINCT s.ip) " +
            "FROM stat s JOIN app a ON a.id = s.app_id " +
            "WHERE s.timestamp > :start AND s.timestamp < :end AND s.url IN (:uris) " +
            "GROUP BY s.url, a.name ORDER BY COUNT(DISTINCT s.ip) DESC")
    List<StatHitCount> findAllWithUrisAndUnique(LocalDateTime start, LocalDateTime end, List<String> uris);
}
