package org.domain.events.domain;

import jakarta.inject.Singleton;

import static org.domain.events.domain.util.IdGenerator.randomId;

@Singleton
public class UserFactoryImpl implements UserFactory {
    @Override
    public User createUser(String name) {
        return User.builder()
                .userId(UserId.of(randomId()))
                .name(name)
                .build();
    }
}
