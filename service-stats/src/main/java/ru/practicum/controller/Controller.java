package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.controller.dto.HitRequestDto;
import ru.practicum.controller.dto.ViewStatsResponseDto;
import ru.practicum.service.StatsService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final StatsService statsService;

    @PostMapping("/hit")
    public ResponseEntity<Void> hit(@RequestBody HitRequestDto hitRequestDto) {
        statsService.hit(hitRequestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<List<ViewStatsResponseDto>> showStats() {
        return ResponseEntity.ok(statsService.showStats());
    }
}
