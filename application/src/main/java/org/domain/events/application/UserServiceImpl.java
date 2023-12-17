package org.domain.events.application;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.domain.events.domain.EventPublisher;
import org.domain.events.domain.Topic;
import org.domain.events.domain.User;
import org.domain.events.domain.UserCreatedEvent;
import org.domain.events.domain.UserFactory;
import org.domain.events.domain.UserRepository;
import org.domain.events.domain.UserService;

@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Singleton
public class UserServiceImpl implements UserService {
    @Inject
    UserFactory userFactory;
    @Inject
    UserRepository userRepository;
    @Inject
    EventPublisher eventPublisher;

    @Transactional
    @Override
    public User createUser(String name) {
        log.info("Creating user with name: {}", name);
        User user = userFactory.createUser(name);
        log.info("Creating user: {}", user);
        userRepository.saveUser(user);

        UserCreatedEvent userCreatedEvent = UserCreatedEvent.of(user);
        eventPublisher.publish(Topic.USER_CREATED, userCreatedEvent);

        return user;
    }
}
