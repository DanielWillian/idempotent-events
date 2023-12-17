package org.domain.events.domain;

@FunctionalInterface
public interface EventListener<T extends DomainEvent> {
    void process(T event);
    @SuppressWarnings("unchecked")
    default void processEvent(DomainEvent event) {
        process((T) event);
    }
}
