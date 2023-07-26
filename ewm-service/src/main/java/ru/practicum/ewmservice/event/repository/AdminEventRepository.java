package ru.practicum.ewmservice.event.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.ewmservice.event.dao.EventEntity;
import ru.practicum.ewmservice.state.SortState;
import ru.practicum.ewmservice.state.State;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ru.practicum.ewmservice.state.State.PUBLISHED;

@Repository
public class AdminEventRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<EventEntity> findEvents(List<Long> users, List<State> states,
                                        List<Long> categories, LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd, int from, int size) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<EventEntity> query = builder.createQuery(EventEntity.class);
        Root<EventEntity> event = query.from(EventEntity.class);
        Predicate criteria = builder.conjunction();

        if (users != null) {
            Predicate inUsers = event.get("initiator").in(users);
            criteria = builder.and(criteria, inUsers);
        }
        if (states != null) {
            Predicate inStates = event.get("state").in(states);
            criteria = builder.and(criteria, inStates);
        }
        if (categories != null) {
            Predicate inCategories = event.get("category").in(categories);
            criteria = builder.and(criteria, inCategories);
        }
        if (rangeStart != null) {
            Predicate start = builder.greaterThan(event.get("eventDate"), rangeStart);
            criteria = builder.and(criteria, start);
        }
        if (rangeEnd != null) {
            Predicate end = builder.lessThan(event.get("eventDate"), rangeEnd);
            criteria = builder.and(criteria, end);
        }
        Predicate startPage = builder.greaterThanOrEqualTo(event.get("id"), from);
        criteria = builder.and(criteria, startPage);
        query.select(event).where(criteria).orderBy(builder.desc(event.get("id")));
        return entityManager.createQuery(query).setMaxResults(size).getResultList();
    }

    public List<EventEntity> findPublicEventsWithParameters(String text, List<Long> categories,
                                                      Boolean paid, String rangeStart, String rangeEnd,
                                                      SortState sort, int from,
                                                      int size) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<EventEntity> query = builder.createQuery(EventEntity.class);
        Root<EventEntity> event = query.from(EventEntity.class);
        Predicate criteria = builder.conjunction();

        if (categories != null) {
            Predicate inCategories = event.get("category").in(categories);
            criteria = builder.and(criteria, inCategories);
        }
        if (!text.isEmpty()) {
            Predicate annotation = builder.like(builder.lower(event.get("annotation")),
                    "%" + text.toLowerCase() + "%");
            Predicate description = builder.like(builder.lower(event.get("description")),
                    "%" + text.toLowerCase() + "%");
            Predicate hasText = builder.or(annotation, description);
            criteria = builder.and(criteria, hasText);
        }
        if (rangeStart != null) {
            LocalDateTime rangeStartTime = LocalDateTime.parse(rangeStart, dateTimeFormatter);
            Predicate start = builder.greaterThan(event.get("eventDate"), rangeStartTime);
            criteria = builder.and(criteria, start);
        }
        if (rangeEnd != null) {
            LocalDateTime rangeEndTime = LocalDateTime.parse(rangeEnd, dateTimeFormatter);
            Predicate end = builder.lessThan(event.get("eventDate"), rangeEndTime);
            criteria = builder.and(criteria, end);
        }
        if (paid != null) {
            Predicate paidPred = builder.equal(event.get("paid"), paid);
            criteria = builder.and(criteria, paidPred);
        }
        Predicate statsEvent = event.get("state").in(PUBLISHED);
        criteria = builder.and(criteria, statsEvent);
        if (sort != null && sort.equals(SortState.EVENT_DATE)) {
            query.select(event).where(criteria).orderBy(builder.desc(event.get("eventDate")));
        } else {
            query.select(event).where(criteria);
        }
        return entityManager.createQuery(query).setFirstResult(from).setMaxResults(size).getResultList();
    }
}
