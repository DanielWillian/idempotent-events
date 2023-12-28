package org.domain.events.application;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import lombok.SneakyThrows;
import org.domain.events.domain.OutboxMessage;
import org.domain.events.domain.OutboxService;
import org.domain.events.domain.OutboxWrapper;

import java.util.Collection;

@EarlyEventDelivery
@Priority(Interceptor.Priority.PLATFORM_BEFORE + 100)
@Interceptor
public class EventInterceptor {
    @Inject
    OutboxService outboxService;

    @SneakyThrows
    @AroundInvoke
    Object sendMessages(InvocationContext context) {
        Object ret = context.proceed();
        if (ret instanceof OutboxMessage message) {
            deliverMessage(message);
        }
        if (ret instanceof Collection<?> collection) {
            for (Object o : collection) {
                if (o instanceof OutboxMessage message) {
                    deliverMessage(message);
                }
            }
        }
        if (ret instanceof OutboxWrapper wrapper) {
            for (Object o : wrapper.getMessages()) {
                if (o instanceof OutboxMessage message) {
                    deliverMessage(message);
                }
            }
        }
        return ret;
    }

    private void deliverMessage(OutboxMessage message) {
        Uni.createFrom()
                .item(message)
                .emitOn(Infrastructure.getDefaultWorkerPool())
                .subscribe()
                .with(outboxService::deliverMessage);
    }
}
