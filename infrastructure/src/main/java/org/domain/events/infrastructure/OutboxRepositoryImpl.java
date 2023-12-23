package org.domain.events.infrastructure;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.domain.events.domain.EventId;
import org.domain.events.domain.OutboxMessage;
import org.domain.events.domain.OutboxRepository;
import org.domain.events.domain.Topic;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Singleton
public class OutboxRepositoryImpl implements OutboxRepository {
    private static final Duration POLL_DELAY = Duration.ofSeconds(5);
    private static final int MAX_RESULTS_OUTBOX_FETCH = 100;
    @Inject
    private EntityManager entityManager;
    @Inject
    private Clock clock;

    @Override
    public void saveMessage(OutboxMessage message) {
        OutboxEntity entity = OutboxEntity.builder()
                .id(message.getEventId().getId())
                .topic(message.getTopic().toString())
                .payload(message.getMessage())
                .updateDate(message.getUpdateDate())
                .build();

        entityManager.persist(entity);
    }

    @Override
    public List<OutboxMessage> fetchMessages() {
        LocalDateTime now = LocalDateTime.ofInstant(clock.instant(), clock.getZone());
        LocalDateTime fiveSecondsEarlier = now.minus(POLL_DELAY);
        List<OutboxEntity> outboxEntities = entityManager
                .createQuery("SELECT o FROM OutboxEntity o WHERE updateDate < :date", OutboxEntity.class)
                .setParameter("date", fiveSecondsEarlier)
                .setMaxResults(MAX_RESULTS_OUTBOX_FETCH)
                .getResultList();

        if (!outboxEntities.isEmpty()) {
            List<String> ids = outboxEntities.stream()
                    .map(OutboxEntity::getId)
                    .toList();

            entityManager
                    .createQuery("UPDATE OutboxEntity SET updateDate = :date WHERE id in (:ids)")
                    .setParameter("date", now)
                    .setParameter("ids", ids)
                    .executeUpdate();
        }

        return outboxEntities.stream()
                .map(OutboxRepositoryImpl::toDomain)
                .toList();
    }

    private static OutboxMessage toDomain(OutboxEntity entity) {
        return OutboxMessage.builder()
                .eventId(EventId.of(entity.getId()))
                .topic(Topic.valueOf(entity.getTopic()))
                .message(entity.getPayload())
                .updateDate(entity.getUpdateDate())
                .build();
    }

    @Override
    public void deleteMessage(EventId eventId) {
        entityManager
                .createQuery("DELETE FROM OutboxEntity WHERE id = :id")
                .setParameter("id", eventId.getId())
                .executeUpdate();
    }
}
