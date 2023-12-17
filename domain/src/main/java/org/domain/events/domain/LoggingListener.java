package org.domain.events.domain;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingListener implements EventListener<DomainEvent> {
    @Override
    public void process(DomainEvent event) {
        log.info("Processing event: {}", event);
    }
}
