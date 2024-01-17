package org.domain.events.infrastructure.idempotency;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.domain.events.domain.idempotency.Idempotency;
import org.domain.events.domain.idempotency.IdempotencyId;
import org.domain.events.domain.idempotency.IdempotencyRepository;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Singleton
public class IdempotencyRepositoryImpl implements IdempotencyRepository {
    @Inject
    private EntityManager entityManager;
    @Inject
    private Clock clock;

    @Override
    public Optional<Idempotency> getIdempotency(IdempotencyId id) {
        Optional<IdempotencyEntity> entity = entityManager
                .createQuery("SELECT i FROM IdempotencyEntity i WHERE id = :id", IdempotencyEntity.class)
                .setParameter("id", id.getId())
                .getResultStream()
                .findAny();

        return entity.map(IdempotencyRepositoryImpl::toDomainIdempotency);
    }

    private static Idempotency toDomainIdempotency(IdempotencyEntity entity) {
        return Idempotency.builder()
                .idempotencyId(IdempotencyId.of(entity.getId()))
                .creationDate(entity.getCreationDate())
                .build();
    }

    @Override
    public void saveIdempotency(IdempotencyId id) {
        log.info("Saving idempotency, id: {}", id.getId());
        IdempotencyEntity entity = IdempotencyEntity.builder()
                .id(id.getId())
                .creationDate(LocalDateTime.ofInstant(clock.instant(), clock.getZone()))
                .build();
        entityManager.persist(entity);
    }
}
