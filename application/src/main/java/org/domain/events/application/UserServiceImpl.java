package org.domain.events.application;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.domain.events.domain.EventPublisher;
import org.domain.events.domain.Topic;
import org.domain.events.domain.User;
import org.domain.events.domain.UserCreatedEvent;
import org.domain.events.domain.UserFactory;
import org.domain.events.domain.UserRepository;
import org.domain.events.domain.UserService;

@AllArgsConstructor
@NoArgsConstructor
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
        User user = userFactory.createUser(name);
        userRepository.saveUser(user);

        UserCreatedEvent userCreatedEvent = UserCreatedEvent.of(user);
        eventPublisher.publish(Topic.USER_CREATED, userCreatedEvent);

        return user;
    }
}
