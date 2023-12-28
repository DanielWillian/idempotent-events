package org.domain.events.domain;

public interface UserService {
    User createUserBestEffort(String name);
    User createUserOutboxPoll(String name);
}
