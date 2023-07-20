package ru.practicum.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StatHitCount {
    private String url;
    private String app;
    private Long hits;
}
