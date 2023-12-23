package org.domain.events.domain;

import java.util.List;

public interface OutboxRepository {
    void saveMessage(OutboxMessage message);

    List<OutboxMessage> fetchMessages();

    void deleteMessage(EventId eventId);
}
