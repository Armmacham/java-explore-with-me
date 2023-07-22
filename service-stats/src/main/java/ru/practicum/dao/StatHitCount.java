package ru.practicum.dao;

import lombok.Getter;

@Getter
public class StatHitCount {
    private String uri;
    private String app;
    private Long hits;

    public StatHitCount(String uri, String app, Long hits) {
        this.uri = uri;
        this.app = app;
        this.hits = hits;
    }
}
