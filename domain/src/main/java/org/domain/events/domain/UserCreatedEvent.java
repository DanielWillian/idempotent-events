package org.domain.events.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
@AllArgsConstructor(staticName = "of")
public class UserCreatedEvent extends DomainEvent {
    User user;
}
