package ru.practicum.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends CrudRepository<StatsEntity, Long> {

    @Query("SELECT new ru.practicum.dao.StatHitCount(s.url, s.app.name, COUNT(s.url)) " +
            "FROM StatsEntity s " +
            "where s.timestamp > :start and s.timestamp < :end " +
            "group by s.url, s.app.name order by COUNT(s.url) desc")
    List<StatHitCount> findAllWithoutUriAndNotUnique(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.dao.StatHitCount(s.url, s.app.name, COUNT(distinct s.ip)) " +
            "FROM StatsEntity s " +
            "where s.timestamp > :start and s.timestamp < :end " +
            "group by s.url, s.app.name order by COUNT(distinct s.ip) desc")
    List<StatHitCount> findAllWithoutUriAndUnique(LocalDateTime start, LocalDateTime end);

    @Query(nativeQuery = true, value = "select s.url, a.name, count(s.url) from stat s join app a on a.id = s.app_id" +
            " where s.timestamp > :start and s.timestamp < :end and s.url in (:uris) " +
            "group by s.url, a.name order by count(s.url) desc")
    List<StatHitCount> findAllWithUrisAndNotUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.dao.StatHitCount(s.url, s.app.name, COUNT(distinct s.ip)) " +
            "FROM StatsEntity s " +
            "where s.timestamp > :start and s.timestamp < :end and s.url in (:uris) " +
            "group by s.url, s.app.name order by COUNT(distinct s.ip) desc")
    List<StatHitCount> findAllWithUrisAndUnique(LocalDateTime start, LocalDateTime end, List<String> uris);
}
