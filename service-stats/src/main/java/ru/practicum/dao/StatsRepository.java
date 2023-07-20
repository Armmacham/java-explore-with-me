package ru.practicum.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatsRepository extends CrudRepository<StatsEntity, Long> {

    @Query("SELECT new ru.practicum.dao.StatHitCount(s.url, s.app, COUNT(s.url)) from StatsEntity s group by s.url")
    List<StatHitCount> findAllCount();
}
