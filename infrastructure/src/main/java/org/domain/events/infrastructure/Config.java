package org.domain.events.infrastructure;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

import java.time.Clock;

@Dependent
public class Config {
    @Produces
    @Singleton
    public Clock clock() {
        return Clock.systemUTC();
    }
}
