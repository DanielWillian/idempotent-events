package org.domain.events.domain;

public interface OutboxService {
    OutboxMessage saveEvent(Topic topic, DomainEvent event);

    void deliverMessage(OutboxMessage message);
}
