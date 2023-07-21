package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.controller.dto.HitRequestDto;
import ru.practicum.controller.dto.ViewStatsResponseDto;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final StatsService statsService;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostMapping("/hit")
    public ResponseEntity<Void> hit(@RequestBody HitRequestDto hitRequestDto) {
        statsService.hit(hitRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/stats")
    public ResponseEntity<List<ViewStatsResponseDto>> showStats(@RequestParam(value = "start") String star,
                                                                @RequestParam(value = "end") String end,
                                                                @RequestParam(value = "uris", required = false) List<String> uris,
                                                                @RequestParam(value = "unique", defaultValue = "false") boolean unique) {
        LocalDateTime startTime = LocalDateTime.parse(star, dateTimeFormatter);
        LocalDateTime endTime = LocalDateTime.parse(end, dateTimeFormatter);
        return ResponseEntity.ok(statsService.showStats(startTime, endTime, uris, unique));
    }
}
