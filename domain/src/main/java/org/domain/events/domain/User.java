package org.domain.events.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(builderClassName = "Builder")
public class User {
    UserId userId;
    String name;
}
