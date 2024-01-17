package org.domain.events.domain.idempotency;

import java.util.Optional;

public interface IdempotencyRepository {
    Optional<Idempotency> getIdempotency(IdempotencyId id);

    void saveIdempotency(IdempotencyId id);
}
