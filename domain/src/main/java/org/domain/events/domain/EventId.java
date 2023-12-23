package org.domain.events.domain;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(staticName = "of")
public class EventId {
    String id;
}
