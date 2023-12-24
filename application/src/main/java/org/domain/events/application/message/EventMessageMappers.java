package org.domain.events.application.message;

import org.domain.events.domain.DomainEvent;
import org.domain.events.domain.EventId;
import org.domain.events.domain.User;
import org.domain.events.domain.UserCreatedEvent;
import org.domain.events.domain.UserId;

public class EventMessageMappers {
    public static DomainEvent toDomain(EventMessage message) {
        if (message instanceof UserCreatedMessage userCreatedMessage) return toUserCreatedEvent(userCreatedMessage);

        throw new IllegalArgumentException("Unknown message: " + message);
    }

    public static EventMessage fromDomain(DomainEvent event) {
        if (event instanceof UserCreatedEvent userCreatedEvent) return fromUserCreatedEvent(userCreatedEvent);

        throw new IllegalArgumentException("Unknown event: " + event);
    }

    private static UserCreatedEvent toUserCreatedEvent(UserCreatedMessage message) {
        User user = User.builder()
                .userId(UserId.of(message.getUserId()))
                .name(message.getName())
                .build();

        return UserCreatedEvent.builder()
                .user(user)
                .eventId(EventId.of(message.getMessageId()))
                .build();
    }

    private static UserCreatedMessage fromUserCreatedEvent(UserCreatedEvent event) {
        return UserCreatedMessage.builder()
                .messageId(event.getEventId().getId())
                .userId(event.getUser().getUserId().getId())
                .name(event.getUser().getName())
                .build();
    }
}
