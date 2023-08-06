package ru.practicum.ewmservice.rating.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.event.dao.EventEntity;
import ru.practicum.ewmservice.event.service.EventService;
import ru.practicum.ewmservice.exception.InvalidStateException;
import ru.practicum.ewmservice.rating.controller.dto.RatingDto;
import ru.practicum.ewmservice.rating.dao.LikeEntity;
import ru.practicum.ewmservice.rating.mapper.LikeMapper;
import ru.practicum.ewmservice.rating.repository.LikeRepository;
import ru.practicum.ewmservice.request.dao.RequestEntity;
import ru.practicum.ewmservice.request.repository.RequestRepository;
import ru.practicum.ewmservice.user.dao.UserEntity;
import ru.practicum.ewmservice.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewmservice.state.RequestState.CONFIRMED;
import static ru.practicum.ewmservice.state.State.PUBLISHED;

@Slf4j
@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final LikeRepository likeRepository;
    private final UserService userService;
    private final EventService eventService;
    private final RequestRepository requestRepository;
    private final LikeMapper likeMapper;

    @Transactional
    public RatingDto addReactionOnEvent(Long eventId, Long userId, boolean isLike) {
        log.info("Adding reaction on event {} by user {}", eventId, userId);
        UserEntity user = userService.getById(userId);
        EventEntity event = eventService.getById(eventId);

        if (!PUBLISHED.equals(event.getState())) {
            throw new InvalidStateException("event should be published!");
        }

        RequestEntity requestByRequesterAndEvent = requestRepository.getRequestByRequesterAndEvent(user, event)
                .orElseThrow(() -> new InvalidStateException("user not participant of the event!"));

        if (!CONFIRMED.equals(requestByRequesterAndEvent.getStatus())) {
            throw new InvalidStateException("illegal state in request!");
        }

        likeRepository.getByEventAndUserEntity(event, user)
                .ifPresentOrElse(e -> {
                    if (e.isValue() == isLike) {
                        likeRepository.delete(e);
                    } else {
                        likeRepository.delete(e);
                        addLike(user, event);
                    }
                }, () -> addLike(user, event));
        return likeRepository.getByEventId(eventId)
                .map(likeMapper::toDto)
                .orElseThrow(() -> new InvalidStateException("user not participant of the event!"));
    }

    private void addLike(UserEntity user, EventEntity event) {
        LikeEntity like = new LikeEntity();
        like.setEvent(event);
        like.setUserEntity(user);
        like.setValue(true);
        likeRepository.save(like);
    }

    @Transactional(readOnly = true)
    public List<RatingDto> getTopLikesEvents(int from, int size) {
        log.info("Getting top events from {}", from);
        return likeRepository.getTopRatingEvents(PageRequest.of(from / size, size))
                .stream()
                .map(likeMapper::toDto)
                .collect(Collectors.toList());
    }
}
