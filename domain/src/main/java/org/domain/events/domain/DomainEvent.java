package org.domain.events.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import static org.domain.events.domain.util.IdGenerator.randomId;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class DomainEvent {
    private final String id;

    public DomainEvent() {
        this.id = randomId();
    }
}
