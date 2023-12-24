package org.domain.events.infrastructure;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.domain.events.domain.OutboxMessage;
import org.domain.events.domain.OutboxRepository;
import org.domain.events.domain.OutboxService;

import java.util.List;

@Slf4j
@AllArgsConstructor
@ApplicationScoped
public class OutboxPoller {
    @Inject
    private OutboxRepository outboxRepository;
    @Inject
    private OutboxService outboxService;

    @Transactional
    @Scheduled(every = "10s")
    void pollOutbox() {
        List<OutboxMessage> messages = outboxRepository.fetchMessages();
        if (!messages.isEmpty()) {
            log.info("Outbox poll fetched {} messages", messages.size());
        }
        messages.forEach(outboxService::deliverMessage);
    }
}
