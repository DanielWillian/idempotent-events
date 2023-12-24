package org.domain.events.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.domain.events.domain.DomainEvent;
import org.domain.events.domain.EventPublisher;
import org.domain.events.domain.OutboxMessage;
import org.domain.events.domain.OutboxRepository;
import org.domain.events.domain.OutboxService;
import org.domain.events.domain.Topic;
import org.domain.events.application.message.EventMessage;
import org.domain.events.application.message.EventMessageMappers;

import java.time.Clock;
import java.time.LocalDateTime;

@Singleton
@Slf4j
public class OutboxServiceImpl implements OutboxService {
    @Inject
    private OutboxRepository outboxRepository;

    @Inject
    private EventPublisher eventPublisher;

    @Inject
    private Clock clock;

    @Inject
    private ObjectMapper objectMapper;

    @Transactional(Transactional.TxType.MANDATORY)
    @SneakyThrows
    @Override
    public OutboxMessage saveEvent(Topic topic, DomainEvent event) {
        EventMessage eventMessage = EventMessageMappers.fromDomain(event);

        OutboxMessage message = OutboxMessage.builder()
                .eventId(event.getEventId())
                .topic(topic)
                .message(objectMapper.writeValueAsString(eventMessage))
                .updateDate(LocalDateTime.ofInstant(clock.instant(), clock.getZone()))
                .build();

        log.info("Saving outbox message: {}", message);
        outboxRepository.saveMessage(message);

        return message;
    }

    @Transactional
    @SneakyThrows
    @Override
    public void deliverMessage(OutboxMessage message) {
        EventMessage eventMessage = objectMapper.readValue(message.getMessage(), EventMessage.class);

        DomainEvent event = EventMessageMappers.toDomain(eventMessage);

        eventPublisher.publish(message.getTopic(), event);

        log.info("Deleting outbox message with id: {}", message.getEventId());
        outboxRepository.deleteMessage(message.getEventId());
    }
}
