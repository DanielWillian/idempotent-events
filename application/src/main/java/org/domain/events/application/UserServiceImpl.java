package org.domain.events.application;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.domain.events.domain.EventPublisher;
import org.domain.events.domain.OutboxMessage;
import org.domain.events.domain.OutboxService;
import org.domain.events.domain.OutboxWrapper;
import org.domain.events.domain.Topic;
import org.domain.events.domain.User;
import org.domain.events.domain.UserCreatedEvent;
import org.domain.events.domain.UserFactory;
import org.domain.events.domain.UserRepository;
import org.domain.events.domain.UserService;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Singleton
public class UserServiceImpl implements UserService {
    @Inject
    private UserFactory userFactory;
    @Inject
    private UserRepository userRepository;
    @Inject
    private EventPublisher eventPublisher;
    @Inject
    private OutboxService outboxService;

    @Transactional
    @Override
    public User createUserBestEffort(String name) {
        User user = userFactory.createUser(name);
        userRepository.saveUser(user);

        UserCreatedEvent userCreatedEvent = UserCreatedEvent.of(user);
        eventPublisher.publish(Topic.USER_CREATED, userCreatedEvent);

        return user;
    }

    @Transactional
    @Override
    public User createUserOutboxPoll(String name) {
        User user = userFactory.createUser(name);
        userRepository.saveUser(user);

        UserCreatedEvent userCreatedEvent = UserCreatedEvent.of(user);
        outboxService.saveEvent(Topic.USER_CREATED, userCreatedEvent);

        return user;
    }

    @EarlyEventDelivery
    @Transactional
    @Override
    public OutboxWrapper<User> createUserOutboxImmediate(String name) {
        User user = userFactory.createUser(name);
        userRepository.saveUser(user);

        UserCreatedEvent userCreatedEvent = UserCreatedEvent.of(user);
        OutboxMessage message = outboxService.saveEvent(Topic.USER_CREATED, userCreatedEvent);

        return OutboxWrapper.<User>builder()
                .messages(List.of(message))
                .payload(user)
                .build();
    }
}
