package ru.practicum.servicestatsdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViewStatsResponseDto {
    private String uri;
    private String app;
    private Long hits;
}
