package ru.practicum.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StatHitCount {
    private String url;
    private String app;
    private Long hits;
}
