package org.domain.events.domain;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.domain.events.domain.idempotency.IdempotencyId;
import org.domain.events.domain.idempotency.IdempotencyRepository;

@Slf4j
@Singleton
public class LoggingListener implements EventListener<DomainEvent> {
    @Inject
    private IdempotencyRepository idempotencyRepository;

    @Override
    public void process(DomainEvent event) {
        IdempotencyId idempotencyId = IdempotencyId.of(event.getEventId().getId());
        if (idempotencyRepository.getIdempotency(idempotencyId).isPresent()) {
            log.info("Already processed event: {}", event);
            return;
        }

        log.info("Processing event: {}", event);

        idempotencyRepository.saveIdempotency(idempotencyId);
    }
}
