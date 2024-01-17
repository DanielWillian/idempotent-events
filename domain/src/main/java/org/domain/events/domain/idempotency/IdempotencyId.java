package org.domain.events.domain.idempotency;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(staticName = "of")
public class IdempotencyId {
    String id;
}
