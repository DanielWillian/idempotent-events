package org.domain.events.infrastructure;

import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.domain.events.domain.DomainEvent;
import org.domain.events.domain.EventListener;
import org.domain.events.domain.EventPublisher;
import org.domain.events.domain.EventSubscriber;
import org.domain.events.domain.LoggingListener;
import org.domain.events.domain.Topic;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Singleton
@Slf4j
public class EventChannels implements EventSubscriber, EventPublisher {
    private final Map<Topic, List<EventListener<?>>> listeners;

    public EventChannels() {
        listeners = new EnumMap<>(Topic.class);
        addListener(Topic.USER_CREATED, new LoggingListener());
    }

    public EventChannels(Map<Topic, List<EventListener<?>>> listeners) {
        this.listeners = new EnumMap<>(listeners);
    }

    @Override
    public void addListener(Topic topic, EventListener<?> listener) {
        if (!listeners.containsKey(topic)) listeners.put(topic, new ArrayList<>());
        listeners.get(topic).add(listener);
    }

    @Override
    public void publish(Topic topic, DomainEvent event) {
        if (!listeners.containsKey(topic)) return;

        log.info("Publishing event: {}", event);

        for (EventListener<?> listener : listeners.get(topic)) {
            listener.processEvent(event);
        }
    }
}
