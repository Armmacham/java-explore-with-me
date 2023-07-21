package ru.practicum.dao;

import lombok.Getter;

@Getter
public class StatHitCount {
    private String url;
    private String app;
    private Long hits;

    public StatHitCount(String url, String app, Long hits) {
        this.url = url;
        this.app = app;
        this.hits = hits;
    }
}
