package org.domain.events.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor(staticName = "of")
@Builder(builderClassName = "Builder")
public class OutboxWrapper<T> {
    List<OutboxMessage> messages;
    T payload;
}
