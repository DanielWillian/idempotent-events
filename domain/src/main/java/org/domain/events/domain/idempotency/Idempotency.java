package org.domain.events.domain.idempotency;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@AllArgsConstructor(staticName = "of")
@Builder(builderClassName = "Builder")
public class Idempotency {
    IdempotencyId idempotencyId;
    LocalDateTime creationDate;
}
