package ru.practicum.ewmservice.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmservice.event.dao.EventEntity;
import ru.practicum.ewmservice.request.dao.RequestEntity;
import ru.practicum.ewmservice.state.RequestState;
import ru.practicum.ewmservice.user.dao.UserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<RequestEntity, Long> {

    Optional<RequestEntity> getRequestByRequesterAndEvent(UserEntity requester, EventEntity event);

    @Query("SELECT count(r.id) FROM RequestEntity r WHERE r.event.id = :eventId and r.status = :state")
    Integer getConfirmedRequest(Long eventId, RequestState state);

    List<RequestEntity> findAllByIdIn(List<Long> ids);

    List<RequestEntity> getRequestsByRequester(UserEntity requester);

    List<RequestEntity> getRequestsByEvent(EventEntity event);
}
