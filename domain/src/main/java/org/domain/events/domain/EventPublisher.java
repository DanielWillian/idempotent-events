package org.domain.events.domain;

public interface EventPublisher {
    void publish(Topic topic, DomainEvent event);
}
