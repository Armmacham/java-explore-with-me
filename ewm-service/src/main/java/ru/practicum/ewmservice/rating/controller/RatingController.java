package ru.practicum.ewmservice.rating.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewmservice.rating.controller.dto.RatingDto;
import ru.practicum.ewmservice.rating.service.RatingService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/rating")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @ResponseStatus(value = HttpStatus.CREATED)
    @PutMapping("/like/event/{eventId}/user/{userId}")
    public RatingDto likeEvent(@PathVariable Long eventId, @PathVariable Long userId) {
        return ratingService.addReactionOnEvent(eventId, userId, true);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PutMapping("/dislike/event/{eventId}/user/{userId}")
    public RatingDto dislikeEvent(@PathVariable Long eventId, @PathVariable Long userId) {
        return ratingService.addReactionOnEvent(eventId, userId, false);
    }

    @GetMapping("/events")
    public List<RatingDto> getTopRatingEvents(@Min(0) @RequestParam(value = "from", defaultValue = "0") int from,
                                              @Min(1) @RequestParam(value = "size", defaultValue = "10") int size) {
        return ratingService.getTopLikesEvents(from, size);
    }

}
