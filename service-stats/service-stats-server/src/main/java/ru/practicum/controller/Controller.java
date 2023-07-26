package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.service.StatsService;
import ru.practicum.servicestatsdto.HitRequestDto;
import ru.practicum.servicestatsdto.ViewStatsResponseDto;

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
    public ResponseEntity<List<ViewStatsResponseDto>> showStats(@RequestParam(value = "start") String start,
                                                                @RequestParam(value = "end") String end,
                                                                @RequestParam(value = "uris", required = false) List<String> uris,
                                                                @RequestParam(value = "unique", defaultValue = "false") boolean unique) {
        LocalDateTime startTime = LocalDateTime.parse(start, dateTimeFormatter);
        LocalDateTime endTime = LocalDateTime.parse(end, dateTimeFormatter);
        return ResponseEntity.ok(statsService.showStats(startTime, endTime, uris, unique));
    }
}
