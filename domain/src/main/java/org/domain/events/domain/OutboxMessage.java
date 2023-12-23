package org.domain.events.domain;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder(builderClassName = "Builder")
public class OutboxMessage {
    EventId eventId;
    Topic topic;
    String message;
    LocalDateTime updateDate;
}
