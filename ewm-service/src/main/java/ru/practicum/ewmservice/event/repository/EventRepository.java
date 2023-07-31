package ru.practicum.ewmservice.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewmservice.event.controller.dto.EventWithRequestNum;
import ru.practicum.ewmservice.event.dao.EventEntity;
import ru.practicum.ewmservice.state.RequestState;

import java.util.List;


public interface EventRepository extends CrudRepository<EventEntity, Long> {

    @Query("SELECT DISTINCT e FROM EventEntity e WHERE e.initiator.id = :userId")
    Page<EventEntity> findAllUserEvents(Long userId, Pageable pageable);

    @Query("SELECT new ru.practicum.ewmservice.event.controller.dto.EventWithRequestNum(r.event.id, count (r.id)) " +
            "FROM RequestEntity r " +
            "WHERE r.event.id IN :events AND r.status = :status " +
            "GROUP BY r.event.id")
    List<EventWithRequestNum> getConfirmedRequestMap(@Param("events") List<Long> events, @Param("status") RequestState status);

    List<EventEntity> getEventsByIdIn(List<Long> ids);
}
