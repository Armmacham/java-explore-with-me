package ru.practicum.ewmservice.request.dao;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.ewmservice.event.dao.EventEntity;
import ru.practicum.ewmservice.state.RequestState;
import ru.practicum.ewmservice.user.dao.UserEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
@Table(name = "requests")
public class RequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "requester")
    private UserEntity requester;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "event")
    private EventEntity event;

    @Enumerated(EnumType.STRING)
    private RequestState status;

    @DateTimeFormat
    private LocalDateTime created;
}
