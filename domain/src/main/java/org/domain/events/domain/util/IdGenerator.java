package org.domain.events.domain.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IdGenerator {
    public static String randomId() {
        return UUID.randomUUID().toString();
    }
}
