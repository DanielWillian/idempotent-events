package org.domain.events.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
@AllArgsConstructor(staticName = "of")
public class UserCreatedEvent extends DomainEvent {
    User user;

    @Builder
    public UserCreatedEvent(User user, EventId eventId) {
        super(eventId);
        this.user = user;
    }
}
