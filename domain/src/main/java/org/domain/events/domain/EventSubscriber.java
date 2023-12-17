package org.domain.events.domain;

public interface EventSubscriber {
    void addListener(Topic topic, EventListener<?> listener);
}
