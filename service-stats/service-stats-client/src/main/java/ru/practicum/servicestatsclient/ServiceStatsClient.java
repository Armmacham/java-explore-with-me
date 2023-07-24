package ru.practicum.servicestatsclient;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.servicestatsdto.HitRequestDto;
import ru.practicum.servicestatsdto.ViewStatsResponseDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class ServiceStatsClient {

    @Value("${service.stats.url}")
    private String serviceStatsUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void hit(HitRequestDto hitRequestDto) {
        restTemplate.postForLocation(serviceStatsUrl + "/hit", hitRequestDto);
    }

    public List<ViewStatsResponseDto> showStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        return Arrays.asList(Objects.requireNonNull(
                restTemplate.getForEntity(serviceStatsUrl + "/stats?start=" + dateTimeFormatter.format(start) +
                        "&end=" + dateTimeFormatter.format(end) +
                        "&uris=" + uris +
                        "&unique=" + unique,
                ViewStatsResponseDto[].class).getBody()));
    }
}
