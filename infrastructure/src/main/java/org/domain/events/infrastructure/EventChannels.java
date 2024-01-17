package org.domain.events.infrastructure;

import io.quarkus.narayana.jta.QuarkusTransaction;
import io.smallrye.common.annotation.Blocking;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.domain.events.application.message.EventMessage;
import org.domain.events.application.message.EventMessageMappers;
import org.domain.events.domain.DomainEvent;
import org.domain.events.domain.EventListener;
import org.domain.events.domain.EventPublisher;
import org.domain.events.domain.EventSubscriber;
import org.domain.events.domain.LoggingListener;
import org.domain.events.domain.Topic;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Singleton
@Slf4j
public class EventChannels implements EventSubscriber, EventPublisher {
    private final Map<Topic, List<EventListener<?>>> listeners;
    private final Map<Topic, Emitter<EventMessage>> emitters;

    @Inject
    public EventChannels(LoggingListener loggingListener,
                         @Channel("user-created") Emitter<EventMessage> userCreatedEmitter) {
        this(Map.of(Topic.USER_CREATED, List.of(loggingListener)),
                Map.of(Topic.USER_CREATED, userCreatedEmitter));
    }

    public EventChannels(Map<Topic, List<EventListener<?>>> listeners,
                         Map<Topic, Emitter<EventMessage>> emitters) {
        this.listeners = new EnumMap<>(listeners);
        this.emitters = new EnumMap<>(emitters);
    }

    @Override
    public void addListener(Topic topic, EventListener<?> listener) {
        if (!listeners.containsKey(topic)) listeners.put(topic, new ArrayList<>());
        listeners.get(topic).add(listener);
    }

    @Override
    public void publish(Topic topic, DomainEvent event) {
        if (!listeners.containsKey(topic)) return;
        if (!emitters.containsKey(topic)) return;

        EventMessage eventMessage = EventMessageMappers.fromDomain(event);

        log.info("Publishing message: {}", eventMessage);

        emitters.get(topic).send(eventMessage);
    }

    @Incoming("user-created")
    @Blocking
    @Retry
    public void userCreated(EventMessage message) {
        QuarkusTransaction.requiringNew().run(() -> processEvent(Topic.USER_CREATED, message));

        if (Math.random() >= 0.5) throw new RuntimeException("Simulated error after transaction");
    }

    private void processEvent(Topic topic, EventMessage message) {
        log.info("Consuming message: {}", message);

        DomainEvent event = EventMessageMappers.toDomain(message);
        for (EventListener<?> listener : listeners.get(topic)) {
            listener.processEvent(event);
        }
    }
}
