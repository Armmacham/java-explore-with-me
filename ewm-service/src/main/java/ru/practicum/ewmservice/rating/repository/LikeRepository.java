package ru.practicum.ewmservice.rating.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.practicum.ewmservice.event.dao.EventEntity;
import ru.practicum.ewmservice.rating.controller.dto.EventLikeCount;
import ru.practicum.ewmservice.rating.dao.LikeEntity;
import ru.practicum.ewmservice.user.dao.UserEntity;

import java.util.Optional;

public interface LikeRepository extends CrudRepository<LikeEntity, Long> {
    Optional<LikeEntity> getByEventAndUserEntity(EventEntity eventEntity, UserEntity user);

    @Query("SELECT " +
            "new ru.practicum.ewmservice.rating.controller.dto.EventLikeCount(" +
            "le.event, " +
            "SUM(CASE WHEN le.value = true THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN le.value = false THEN 1 ELSE 0 END)) FROM LikeEntity le " +
            "group by le.event " +
            "ORDER BY SUM(CASE WHEN le.value = true THEN 1 ELSE 0 END) DESC")
    Page<EventLikeCount> getTopRatingEvents(Pageable pageable);
}
