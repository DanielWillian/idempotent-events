package org.domain.events.infrastructure;

import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.domain.events.domain.User;
import org.domain.events.domain.UserRepository;

@AllArgsConstructor
@Slf4j
@Singleton
public class UserRepositoryImpl implements UserRepository {
    private final EntityManager entityManager;

    @Override
    public void saveUser(User user) {
        log.info("Creating user: {}", user);

        UserEntity entity = UserEntity.builder()
                .id(user.getUserId().getId())
                .name(user.getName())
                .build();

        entityManager.persist(entity);
    }
}
