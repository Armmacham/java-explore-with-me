package ru.practicum.dao;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "stat")
@NamedNativeQueries({
        @NamedNativeQuery(name = "WithoutUriAndNotUnique", resultSetMapping = "queryToDto",
                query = "SELECT s.url as url, a.name as name, COUNT(s.url) as count " +
                        "FROM stat s JOIN app a ON a.id = s.app_id " +
                        "WHERE s.timestamp > :start AND s.timestamp < :end " +
                        "GROUP BY s.url, a.name ORDER BY COUNT(s.url) DESC"
        ),
        @NamedNativeQuery(name = "WithoutUriAndUnique", resultSetMapping = "queryToDto",
                query = "SELECT s.url as url, a.name as name, COUNT(DISTINCT s.ip) as count " +
                        "FROM stat s JOIN app a ON a.id = s.app_id " +
                        "WHERE s.timestamp > :start AND s.timestamp < :end " +
                        "GROUP BY s.url, a.name ORDER BY COUNT(DISTINCT s.ip) DESC"
        ),
        @NamedNativeQuery(name = "WithUrisAndNotUnique", resultSetMapping = "queryToDto",
                query = "SELECT s.url as url, a.name as name, count(s.url) as count  from stat s join app a on a.id = s.app_id " +
                        "where s.timestamp > :start and s.timestamp < :end and s.url in (:uris) " +
                        "group by s.url, a.name order by count(s.url) desc"
        ),
        @NamedNativeQuery(name = "WithUrisAndUnique", resultSetMapping = "queryToDto",
                query = "SELECT s.url as url, a.name as name, COUNT(DISTINCT s.ip) as count " +
                        "FROM stat s JOIN app a ON a.id = s.app_id " +
                        "WHERE s.timestamp > :start AND s.timestamp < :end AND s.url IN (:uris) " +
                        "GROUP BY s.url, a.name ORDER BY COUNT(DISTINCT s.ip) DESC"
        )
})
@SqlResultSetMapping(name = "queryToDto", classes = {
        @ConstructorResult(
                columns = {
                        @ColumnResult(name = "url", type = String.class),
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "count", type = Long.class),
                },
                targetClass = StatHitCount.class
        )
})
public class StatsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "app_id")
    private AppEntity app;

    private String url;
    private String ip;
    private LocalDateTime timestamp;
    @Transient
    private Long hits;
}
